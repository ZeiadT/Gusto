package iti.mad.gusto.presentation.main.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.data.repo.PlanRepository;
import iti.mad.gusto.domain.entity.PlanMealEntity;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.mealdetails.MealDetailsActivity;

public class PlanFragment extends Fragment implements PlanContract.View {

    private DatePicker spinnerDatePicker;
    private View emptyView;
    private RecyclerView planRecyclerView;
    private PlanPresenter presenter;
    private PlanAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new PlanPresenter(this, requireContext());

        initViews(view);
        setupRecyclerView();
        setupDatePicker();

        presenter.getMealsForToday();
    }

    private void initViews(View view) {
        spinnerDatePicker = view.findViewById(R.id.datePicker);
        emptyView = view.findViewById(R.id.emptyView);
        planRecyclerView = view.findViewById(R.id.recyclerViewPlan);
    }

    private void setupRecyclerView() {
        planRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new PlanAdapter(meal -> presenter.onMealClicked(meal));
        planRecyclerView.setAdapter(adapter);

        setupSwipeToDelete();
    }

    private void setupDatePicker() {
        spinnerDatePicker.setMinDate(System.currentTimeMillis());

        spinnerDatePicker.init(
                spinnerDatePicker.getYear(),
                spinnerDatePicker.getMonth(),
                spinnerDatePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) ->
                        presenter.getMealsByDate(year, monthOfYear, dayOfMonth)
        );
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getBindingAdapterPosition();
                        PlanMealEntity meal = adapter.getByPosition(position);

                        presenter.deleteMeal(meal, position);
                    }
                };

        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(planRecyclerView);
    }

    @Override
    public void showMeals(List<PlanMealEntity> plannedMeals) {
        adapter.setPlans(plannedMeals);
    }

    @Override
    public void showEmptyState() {
        emptyView.setVisibility(View.VISIBLE);
        planRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyState() {
        emptyView.setVisibility(View.GONE);
        planRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToMealDetails(String mealId) {
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("mealId", mealId);
        startActivity(intent);
    }

    @Override
    public void removeMealFromAdapter(int position) {
        adapter.removeByPosition(position);

        if (adapter.getItemCount() == 0) {
            showEmptyState();
        }
    }

    @Override
    public void showError(String message) {
        ThemeAwareIconToast.error(requireContext(), message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}