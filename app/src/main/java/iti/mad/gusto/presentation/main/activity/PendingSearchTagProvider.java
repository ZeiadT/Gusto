package iti.mad.gusto.presentation.main.activity;

import iti.mad.gusto.domain.entity.SearchTagEntity;

/**
 * Provides a pending search tag that was requested before SearchFragment was created.
 * MainActivity stores the tag when {@link BottomNavBarCommunicator#navigateToSearchWithTag} is
 * called; SearchFragment consumes it in onResume via this interface.
 */
public interface PendingSearchTagProvider {
    /** Returns the pending tag if any and clears it; returns null otherwise. */
    SearchTagEntity getAndClearPendingSearchTag();
}
