package com.huaxixingfu.sqj.widget;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class MyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        afterTextChanged();
    }

    public abstract void afterTextChanged();
}
