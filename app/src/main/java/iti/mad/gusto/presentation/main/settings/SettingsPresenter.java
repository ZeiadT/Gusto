package iti.mad.gusto.presentation.main.settings;

import android.content.Context;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.core.managers.LocalizationManager;
import iti.mad.gusto.core.managers.ThemeManager;
import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.FavouriteRepository;
import iti.mad.gusto.data.repo.PlanRepository;
import iti.mad.gusto.data.repo.SettingsRepository;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View view;
    private final SettingsRepository settingsRepository;
    private final AuthRepository authRepository;
    private final PlanRepository planRepository;
    private final FavouriteRepository favouriteRepository;
    private final ThemeManager themeManager;
    private final LocalizationManager localizationManager;
    private final CompositeDisposable disposables;

    SettingsPresenter(Context context, SettingsContract.View view) {
        this.view = view;
        this.authRepository = AuthRepository.getInstance(context);
        this.settingsRepository = SettingsRepository.getInstance(context);
        this.planRepository = PlanRepository.getInstance(context);
        this.favouriteRepository = FavouriteRepository.getInstance(context);
        this.themeManager = ThemeManager.getInstance();
        this.localizationManager = LocalizationManager.getInstance();
        disposables = new CompositeDisposable();
    }

    @Override
    public void signOut() {
        Disposable d = authRepository.signOut().observeOn(AndroidSchedulers.mainThread()).subscribe(isOk -> {
            if (isOk) {
                settingsRepository.setRememberMe(false);
                planRepository.dropPlanMeals().observeOn(AndroidSchedulers.mainThread()).subscribe();
                favouriteRepository.dropFavMeals().observeOn(AndroidSchedulers.mainThread()).subscribe();
                view.navigateAuth();
            }
        }, t -> view.showError(t.getMessage()));

        disposables.add(d);
    }

    @Override
    public void setThemeDark() {
        themeManager.setDark();

    }

    @Override
    public void setThemeLight() {
        themeManager.setLight();
    }

    @Override
    public void setThemeSystem() {
        themeManager.setFollowSystem();
    }

    @Override
    public void setLanguageAr() {
        localizationManager.setArabic();
    }

    @Override
    public void setLanguageEn() {
        localizationManager.setEnglish();
    }


    @Override
    public void onDetach() {
        disposables.clear();
    }
}
