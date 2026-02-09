package iti.mad.gusto.presentation.main.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.presentation.mealdetails.MealDetailsActivity;
import iti.mad.gusto.ui.adapter.SearchTagAdapter;

public class SearchFragment extends Fragment implements SearchContract.View {
    TextInputEditText searchMealEditText;
    MaterialAutoCompleteTextView searchTagEditText;
    Button clearBtn;
    RecyclerView tagsRecyclerView;
    RecyclerView mealsRecyclerView;
    SelectedTagAdapter selectedTagsAdapter;
    SearchTagAdapter searchTagsAdapter;
    MealSearchAdapter mealSearchAdapter;
    SearchContract.Presenter presenter;


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

        presenter = new SearchPresenter(this);

        initSelectedTagsView();
        initTagSearchBar();
        initMealSearchBar();
        initMealRecyclerView();

    }

    void initMealSearchBar() {
        searchMealEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                presenter.searchForMeals(query.toString());
            }
        });

    }
    void initSelectedTagsView() {
        selectedTagsAdapter = new SelectedTagAdapter();
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        tagsRecyclerView.setAdapter(selectedTagsAdapter);
    }
    void initTagSearchBar() {
        searchTagsAdapter = new SearchTagAdapter(requireContext(), new ArrayList<>());
        searchTagEditText.setAdapter(searchTagsAdapter);
        searchTagEditText.setThreshold(3);

        searchTagEditText.setOnItemClickListener((parent, view2, position, id) -> {
            SearchTagEntity selectedItem = (SearchTagEntity) parent.getItemAtPosition(position);
            selectedTagsAdapter.addTag(selectedItem);
            searchTagEditText.setText("");
            presenter.searchForMeals(Objects.requireNonNull(searchMealEditText.getText()).toString());
        });
        clearBtn.setOnClickListener(v -> {
            selectedTagsAdapter.setTags(new ArrayList<>());
        });
        searchTagEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.searchForTag(s.toString());

            }
        });
    }
    void initMealRecyclerView() {
        mealSearchAdapter = new MealSearchAdapter(requireContext(), new MealSearchAdapter.OnMealClickListener(){

            @Override
            public void onMealClick(MealEntity meal) {
                Intent intent = new Intent(requireContext(), MealDetailsActivity.class);
                intent.putExtra("mealId", meal.getId());
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(MealEntity meal, boolean isFavorite) {

            }
        });

        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mealsRecyclerView.setAdapter(mealSearchAdapter);
    }


    @Override
    public List<SearchTagEntity> getSelectedTags() {
        return selectedTagsAdapter.getTags();
    }

    @Override
    public void showSelectedTags(List<SearchTagEntity> results) {

    }

    @Override
    public void showSearchTags(List<SearchTagEntity> results) {
        searchTagsAdapter.updateData(results);
    }

    @Override
    public void showMeals(List<MealEntity> meals) {
        mealSearchAdapter.setList(meals);
    }
}

/*
 * Plan
 *
 * 1- text field for tags search
 * 2- dropdown list of possible tag options
 *
 * 3- search field for meals
 *
 * 4- list of selected tags with close icon
 * 5- recycler view with search results
 *
 * 6- on click go to details
 * 7- can add directly to favs
 *
 *
 *
 *
 * */

/*
 * Fixes
 *
 * color mark each tag (category, ingredient, country)
 * don't allow duplication
 * autohint for fields
 *
 * solve focus behaviour
 *
 * */