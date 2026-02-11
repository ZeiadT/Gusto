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
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.mealdetails.MealDetailsActivity;

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

        initViews();

        if (savedInstanceState != null) {
            presenter.restoreState(
                    savedInstanceState.getParcelableArrayList("selectedTags"),
                    savedInstanceState.getString("searchQuery"),
                    savedInstanceState.getParcelableArrayList("searchedMeals")
            );
        } else if (getArguments() != null) {
            SearchFragmentArgs args = SearchFragmentArgs.fromBundle(getArguments());
            if (args.getSearchTag() != null) {
                presenter.onTagSelected(args.getSearchTag());
            }
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
        clearBtn.setOnClickListener(v -> presenter.onClearTagsClicked());

        // Selected Tags -- On Tag Removed
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
            public void onFavoriteClick(MealEntity meal, boolean isFavorite) {/*todo implement add to favorite button*/}
        });


        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        tagsRecyclerView.setAdapter(selectedTagsAdapter);

        searchTagEditText.setAdapter(searchTagsAdapter);

        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mealsRecyclerView.setAdapter(mealSearchAdapter);
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
    public void showMeals(List<MealEntity> meals) {
        mealSearchAdapter.setList(meals);
    }

    @Override
    public void showError(String errMsg) {
        ThemeAwareIconToast.error(requireContext(), errMsg);
    }
}