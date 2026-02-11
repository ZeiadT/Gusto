package iti.mad.gusto.presentation.main.discover;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.CountryEntity;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.presentation.common.component.AddToPlanBottomSheet;
import iti.mad.gusto.presentation.common.component.FeaturedMealCard;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.mealdetails.MealDetailsActivity;

public class DiscoverFragment extends Fragment implements DiscoverContract.View {
    RecyclerView recyclerView;
    FeaturedMealCard cardDailySpecial;
    ChipGroup countriesGroup;
    CategoriesAdapter categoryAdapter;
    DiscoverContract.Presenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DiscoverPresenter(requireContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardDailySpecial = view.findViewById(R.id.cardDailySpecial);
        countriesGroup = view.findViewById(R.id.chipGroupCountries);

        categoryAdapter = new CategoriesAdapter(cat -> navigateToSearchWithTag(cat.toTag()));

        recyclerView = view.findViewById(R.id.categories_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewCreated();
        //todo add presenter logic for these click listeners
        cardDailySpecial.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
            intent.putExtra("mealId", cardDailySpecial.getMealId());
            startActivity(intent);
        });
        cardDailySpecial.setOnAddClickListener(v -> {
            AddToPlanBottomSheet bottomSheet = AddToPlanBottomSheet.newInstance();
            bottomSheet.show(getParentFragmentManager(), "AddToPlanBottomSheet");
            bottomSheet.setOnConfirmListener((date, mealType) -> {
                presenter.onFeaturedMealAddToPlan(date, mealType);
            });
        });
        cardDailySpecial.setOnFavoriteClickListener(v -> {
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void setFeaturedMeal(MealEntity meal) {
        cardDailySpecial.setMealData(meal.getName(), meal.getCategory(), meal.getImage());
        cardDailySpecial.setMealId(meal.getId());

    }

    @Override
    public void setCategories(List<CategoryEntity> categories) {
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void setCountries(List<CountryEntity> countries) {
        countriesGroup.removeAllViews();

        for (CountryEntity country : countries) {
            addCountryChip(country);
        }
    }

    @Override
    public void showError(String errMsg) {
        ThemeAwareIconToast.error(requireContext(), errMsg);
    }

    private void addCountryChip(CountryEntity country) {
        Chip chip = (Chip) LayoutInflater.from(this.requireContext())
                .inflate(R.layout.item_chip_country, countriesGroup, false);

        chip.setText(country.getName());

        chip.setOnClickListener(v -> navigateToSearchWithTag(country.toTag()));

        countriesGroup.addView(chip);
    }

    private void navigateToSearchWithTag(SearchTagEntity tag) {

        DiscoverFragmentDirections.ActionDiscoverToSearch action =
                DiscoverFragmentDirections.actionDiscoverToSearch()
                        .setSearchTag(tag);

        if (getView() != null) {
            Navigation.findNavController(getView()).navigate(action);
        }
    }

}