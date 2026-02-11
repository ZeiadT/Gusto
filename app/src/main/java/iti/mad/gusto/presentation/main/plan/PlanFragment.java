package iti.mad.gusto.presentation.main.plan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.PlanMealEntity;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.mealdetails.MealDetailsActivity;

public class PlanFragment extends Fragment implements PlanContract.View {

    private DatePicker spinnerDatePicker;
    private PlanPresenter presenter;
    PlanAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new PlanPresenter(requireContext(), this);
        spinnerDatePicker = view.findViewById(R.id.datePicker);

        RecyclerView rv = view.findViewById(R.id.recyclerViewPlan);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new PlanAdapter(meal -> {
            navigateToDetails(meal.getId());
        });

        rv.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDayString = currentDay < 10 ? "0" + currentDay : String.valueOf(currentDay);
        String currentMonthString = (currentMonth + 1) < 10 ? "0" + (currentMonth + 1) : String.valueOf(currentMonth + 1);

        String currentDate = currentDayString + "/" + currentMonthString + "/" + currentYear;
        Log.d("TAG", "onViewCreated: fetched meals for date " + currentDate);

        presenter.getMealsByDate(currentDate);

        spinnerDatePicker.init(currentYear, currentMonth, currentDay, (datePicker, year, monthOfYear, dayOfMonth) -> {
            String DayString = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            String MonthString = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);

            String date = DayString + "/" + MonthString + "/" + year;

            presenter.getMealsByDate(date);
            Log.d("TAG", "onViewCreated: fetched meals for date " + date);
        });
        spinnerDatePicker.setMinDate(System.currentTimeMillis());
    }

    @Override
    public void showMeals(List<PlanMealEntity> plannedMeals) {
        adapter.setPlans(plannedMeals);
    }

    @Override
    public void showMealDetails(String mealId) {
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("mealId", mealId);
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        ThemeAwareIconToast.error(requireContext(), message);
    }

    private void navigateToDetails(String mealId){
        Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
        intent.putExtra("mealId", mealId);
        startActivity(intent);

    }
}