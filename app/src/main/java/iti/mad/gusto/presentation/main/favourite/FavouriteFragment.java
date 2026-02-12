package iti.mad.gusto.presentation.main.favourite;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.mealdetails.MealDetailsActivity;

public class FavouriteFragment extends Fragment implements FavouriteContract.View {
    RecyclerView recyclerView;
    FavouriteAdapter adapter;
    FavouritePresenter presenter;
    View emptyView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_recipes);
        emptyView = view.findViewById(R.id.emptyView);


        adapter = new FavouriteAdapter(new FavouriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavouriteMealEntity meal) {
                navigateToDetails(meal.getId());
            }

            @Override
            public void onFavClick(FavouriteMealEntity meal) {
                presenter.deleteMeal(meal);
            }
        });
        recyclerView.setAdapter(adapter);

        presenter = new FavouritePresenter(requireContext(), this);

        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getMeals();
    }

    @Override
    public void showMeals(List<FavouriteMealEntity> meals) {

        if (meals.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.setMeals(meals);
    }

    @Override
    public void showError(String message) {
        ThemeAwareIconToast.error(requireContext(), message);
    }

    private void navigateToDetails(String mealId) {
        Intent intent = new Intent(getActivity(), MealDetailsActivity.class);
        intent.putExtra("mealId", mealId);
        startActivity(intent);
    }
}