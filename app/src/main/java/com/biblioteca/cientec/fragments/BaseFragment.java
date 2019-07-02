package com.biblioteca.cientec.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseFragment extends Fragment {

    protected void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
