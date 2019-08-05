package gr16.android.chattyfix.interfaces;

import android.view.View;

/**
 * Used by the recycler adapters. Parent activities using recycler views will implement this method
 * to program responses the items being clicked.
 */
public interface ItemClickListener {
    void onItemClick(View view, int position);
}