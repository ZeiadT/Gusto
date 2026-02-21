package iti.mad.gusto.presentation.main.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.presentation.common.component.RippleOverlayView;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToastWithVibration;
import iti.mad.gusto.presentation.common.util.WaveEffectManager;
import iti.mad.gusto.presentation.main.activity.PendingSearchTagProvider;
import iti.mad.gusto.presentation.mealdetails.MealDetailsActivity;

public class SearchFragment extends Fragment implements SearchContract.View, TagReceiver {
    TextInputEditText searchMealEditText;
    MaterialAutoCompleteTextView searchTagEditText;
    Button clearBtn;
    RecyclerView tagsRecyclerView;
    RecyclerView mealsRecyclerView;
    SelectedTagAdapter selectedTagsAdapter;
    SearchTagAdapter searchTagsAdapter;
    MealSearchAdapter mealSearchAdapter;
    SearchContract.Presenter presenter;

    View connectionLottie;
    View appBarLayout;
    View emptyView;
    private RippleOverlayView rippleOverlay;
    private View rootLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tagsRecyclerView = view.findViewById(R.id.tags_recyclerview);
        mealsRecyclerView = view.findViewById(R.id.meals_recyclerview);
        searchTagEditText = view.findViewById(R.id.tag_search_bar);
        searchMealEditText = view.findViewById(R.id.searchMealEditText);
        clearBtn = view.findViewById(R.id.clearBtn);
        connectionLottie = view.findViewById(R.id.connectionLottie);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        emptyView = view.findViewById(R.id.emptyView);
        rippleOverlay = requireActivity().findViewById(R.id.rippleOverlay);
        rootLayout = requireActivity().findViewById(R.id.main);


        presenter = new SearchPresenter(requireContext(), this);

        initViews();

        if (savedInstanceState != null) {
            presenter.restoreState(
                    savedInstanceState.getParcelableArrayList("selectedTags"),
                    savedInstanceState.getString("searchQuery"),
                    savedInstanceState.getParcelableArrayList("searchedMeals")
            );
        }

        presenter.addConnectivityListener(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof PendingSearchTagProvider) {
            SearchTagEntity tag = ((PendingSearchTagProvider) getActivity()).getAndClearPendingSearchTag();
            if (tag != null && presenter != null) {
                onTagReceived(tag);
            }
        }
        if (presenter != null && mealSearchAdapter != null && mealSearchAdapter.getItemCount() > 0) {
            presenter.refreshFavouriteStates();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("selectedTags", new ArrayList<>(selectedTagsAdapter.getTags()));
        outState.putString("searchQuery", Objects.requireNonNull(searchMealEditText.getText()).toString());
        outState.putParcelableArrayList("searchedMeals", new ArrayList<>(mealSearchAdapter.getList()));
    }

    void initViews() {
        // Tag Search Bar -- On Item Clicked
        searchTagEditText.setOnItemClickListener((parent, view, position, id) -> {
            hideKeyboard();
            SearchTagEntity selectedItem = (SearchTagEntity) parent.getItemAtPosition(position);
            presenter.onTagSelected(selectedItem);
        });

        // Tag Search Bar -- On Text Change
        searchTagEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                presenter.searchForTag(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        // Tag Search Bar -- On Clear Clicked
        clearBtn.setOnClickListener(v -> {
            hideKeyboard();
            presenter.onClearTagsClicked();
        });

        selectedTagsAdapter = new SelectedTagAdapter(tag -> presenter.onTagRemoved(tag));
        searchTagsAdapter = new SearchTagAdapter(requireContext(), new ArrayList<>());

        // Meal Search Bar -- On Text Change
        searchMealEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                presenter.searchForMeals(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        mealSearchAdapter = new MealSearchAdapter(requireContext(), new MealSearchAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(MealEntity meal) {
                presenter.onMealClicked(meal);
            }

            @Override
            public void onFavoriteClick(MealEntity meal, boolean isFavorite, View btn) {
                presenter.onMealFavClicked(meal, isFavorite);

                if (isFavorite && btn.isPressed()) {
                    WaveEffectManager.fireWave(btn, rootLayout, rippleOverlay);
                }
            }
        });


        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        tagsRecyclerView.setAdapter(selectedTagsAdapter);

        searchTagEditText.setAdapter(searchTagsAdapter);

        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mealsRecyclerView.setAdapter(mealSearchAdapter);

        if (mealSearchAdapter.getItemCount() == 0 && !presenter.isNetworkDisconnected(requireContext())) {
            emptyView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSelectedTags(List<SearchTagEntity> tags) {
        selectedTagsAdapter.setTags(tags);
    }

    @Override
    public void clearTagSearchBar() {
        searchTagEditText.setText("");
    }

    @Override
    public void refreshFavouriteStates(Set<String> favouriteIds) {
        if (mealSearchAdapter != null) {
            mealSearchAdapter.setFavouriteIds(favouriteIds);
        }
    }

    @Override
    public void navigateToMealDetails(String mealId) {
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("mealId", mealId);
        startActivity(intent);
    }

    @Override
    public void showSearchTags(List<SearchTagEntity> results) {
        searchTagsAdapter.updateData(results);
    }

    @Override
    public void showMeals(List<MealEntity> meals, Set<String> favouriteIds) {
        if (meals.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            mealsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            mealsRecyclerView.setVisibility(View.VISIBLE);
        }
        mealSearchAdapter.setList(meals, favouriteIds != null ? favouriteIds : new HashSet<>());
    }

    @Override
    public void showError(String errMsg) {
        if (isAdded()) {
            ThemeAwareIconToast.error(requireContext(), errMsg);
        }
    }

    @Override
    public void showWarning(String msg) {
        if (isAdded()) {
            ThemeAwareIconToastWithVibration.warning(requireContext(), msg);
        }
    }


    @Override
    public void onNetworkDisconnected() {

        Handler mainHandler = new Handler(Looper.getMainLooper());

        mainHandler.post(() -> {
            mealsRecyclerView.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.GONE);
            connectionLottie.setVisibility(View.VISIBLE);

        });
    }

    @Override
    public void onNetworkReconnected() {

        Handler mainHandler = new Handler(Looper.getMainLooper());

        mainHandler.post(() -> {
            mealsRecyclerView.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.VISIBLE);
            connectionLottie.setVisibility(View.GONE);
            presenter.searchForMeals(Objects.requireNonNull(searchMealEditText.getText()).toString());
        });
    }


    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view == null) return;

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (presenter != null && !hidden) {
            presenter.refreshFavouriteStates();
        }
    }

    @Override
    public void onTagReceived(SearchTagEntity tag) {
        presenter.onClearTagsClicked();
        presenter.onTagSelected(tag);
    }
}