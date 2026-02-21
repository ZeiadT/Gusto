package iti.mad.gusto.presentation.main.search;

import iti.mad.gusto.domain.entity.SearchTagEntity;

public interface TagReceiver {
    void onTagReceived(SearchTagEntity tag);
}
