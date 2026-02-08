package iti.mad.gusto.presentation.main.discover;

import iti.mad.gusto.domain.entity.CategoryEntity;

@FunctionalInterface
public interface OnCategoryClickListener {
    void onClicked(CategoryEntity category);
}
