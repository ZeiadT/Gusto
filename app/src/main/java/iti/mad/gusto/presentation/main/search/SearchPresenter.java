package iti.mad.gusto.presentation.main.search;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import iti.mad.gusto.R;
import iti.mad.gusto.core.managers.NetworkManager;
import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.FavouriteRepository;
import iti.mad.gusto.data.repo.MealRepository;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;

public class SearchPresenter implements SearchContract.Presenter {
    private final SearchContract.View view;
    private final Context context;
    private final MealRepository mealRepository;
    private final FavouriteRepository favouriteRepository;
    private final AuthRepository authRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();


    private final List<SearchTagEntity> selectedTags = new ArrayList<>();
    private String currentMealQuery = "";


    private final PublishSubject<String> tagSearchSubject = PublishSubject.create();
    private final PublishSubject<String> mealSearchSubject = PublishSubject.create();
    private ConnectivityManager.NetworkCallback connectivityListenerCallback;

    public SearchPresenter(Context context, SearchContract.View view) {
        this.view = view;
        this.context = context;
        this.mealRepository = MealRepository.getInstance();
        this.authRepository = AuthRepository.getInstance(context);
        this.favouriteRepository = FavouriteRepository.getInstance(context);
        setupSearchSubjects();
    }

    private void setupSearchSubjects() {
        // Tag Search Stream
        Disposable tagDisposable = tagSearchSubject
                .debounce(400, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(query -> mealRepository.searchForTag(query)
                        .onErrorResumeNext(throwable -> Observable.empty()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showSearchTags,
                        error -> view.showError(error.getMessage())
                );

        // Meal Search Stream: fetch meals then favourite IDs from DB and show
        Disposable mealDisposable = mealSearchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(query -> mealRepository.searchByNameAndTags(query, selectedTags)
                        .onErrorResumeNext(t -> Observable.empty())
                        .flatMapSingle(meals -> favouriteRepository.getFavouriteIds()
                                .map(ids -> new Pair<>(meals, ids))))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> view.showMeals(pair.first, pair.second),
                        error -> view.showError(error.getMessage())
                );

        disposables.addAll(tagDisposable, mealDisposable);
    }

    @Override
    public void searchForTag(String query) {
        tagSearchSubject.onNext(query);
    }

    @Override
    public void searchForMeals(String query) {
        this.currentMealQuery = query;
        mealSearchSubject.onNext(query);
    }

    @Override
    public void onTagSelected(SearchTagEntity tag) {
        for (SearchTagEntity exist : selectedTags) {
            if (exist.getTagType() == tag.getTagType() && exist.getTagName().equals(tag.getTagName()))
                return;
        }

        selectedTags.add(tag);
        updateViewAndRefreshSearch();
        view.clearTagSearchBar();
    }

    @Override
    public void onTagRemoved(SearchTagEntity tag) {
        if (selectedTags.remove(tag)) {
            updateViewAndRefreshSearch();
        }
    }

    @Override
    public void onClearTagsClicked() {
        selectedTags.clear();
        updateViewAndRefreshSearch();
    }

    @Override
    public void onMealClicked(MealEntity meal) {
        if (meal != null && meal.getId() != null) {
            view.navigateToMealDetails(meal.getId());
        }
    }

    @Override
    public void onMealFavClicked(MealEntity meal, boolean isFavorite) {
        if (authRepository.isAnonymousUser()) {
            String pleaseSignIn = context.getString(R.string.please_sign_in);
            view.showWarning(pleaseSignIn);
            return;
        }

        FavouriteMealEntity entity = new FavouriteMealEntity(meal.getId(), meal.getName(), meal.getImage(), meal.getCategory(), meal.getArea());
        Disposable d = (isFavorite
                ? favouriteRepository.addFavourite(entity)
                : favouriteRepository.deleteFavouriteById(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Disposable refresh = favouriteRepository.getFavouriteIds()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            view::refreshFavouriteStates,
                                            t -> view.showError(t.getMessage())
                                    );
                            disposables.add(refresh);
                        },
                        t -> view.showError(t.getMessage())
                );
        disposables.add(d);
    }

    @Override
    public void refreshFavouriteStates() {
        Disposable d = favouriteRepository.getFavouriteIds()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::refreshFavouriteStates,
                        t -> view.showError(t.getMessage())
                );
        disposables.add(d);
    }

    @Override
    public void restoreState(List<SearchTagEntity> tags, String query, List<MealEntity> meals) {
        if (tags != null) {
            this.selectedTags.clear();
            this.selectedTags.addAll(tags);
            view.showSelectedTags(selectedTags);
        }

        if (query != null) {
            this.currentMealQuery = query;
        }

        if (meals != null && !meals.isEmpty()) {
            Disposable d = favouriteRepository.getFavouriteIds()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            ids -> view.showMeals(meals, ids),
                            t -> view.showMeals(meals, Collections.emptySet())
                    );
            disposables.add(d);
        } else {
            searchForMeals(currentMealQuery);
        }
    }

    private void updateViewAndRefreshSearch() {
        view.showSelectedTags(new ArrayList<>(selectedTags));
        forceSearchForMeals();
    }

    private void forceSearchForMeals() {
        Disposable d = mealRepository.searchByNameAndTags(currentMealQuery, selectedTags)
                .flatMapSingle(meals -> favouriteRepository.getFavouriteIds()
                        .map(ids -> new Pair<>(meals, ids)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> view.showMeals(pair.first, pair.second),
                        error -> view.showError(error.getMessage())
                );
        disposables.add(d);
    }


    @Override
    public void addConnectivityListener(Context context) {
        if (NetworkManager.isNetworkDisconnected(context)){
            view.onNetworkDisconnected();
        }

        connectivityListenerCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                view.onNetworkReconnected();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                view.onNetworkDisconnected();
            }
        };

        NetworkManager.addConnectivityListener(context, connectivityListenerCallback);
    }

    @Override
    public void removeConnectivityListener(Context context) {
        NetworkManager.removeConnectivityListener(context, connectivityListenerCallback);

    }


    @Override
    public boolean isNetworkDisconnected(Context context) {
        return NetworkManager.isNetworkDisconnected(context);
    }


    @Override
    public void onDetach() {
        removeConnectivityListener(context);
        disposables.clear();
    }

}