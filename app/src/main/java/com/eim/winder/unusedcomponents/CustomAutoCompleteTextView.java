package com.eim.winder.unusedcomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Mari on 09.02.2016.
 * Custom autocomplete textview. Needs a custom array adapter and custom text changed listener.
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView{
    public CustomAutoCompleteTextView(Context context) {
        super(context);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Disables AutoCompleteTextView text filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String textFilter = "";
        super.performFiltering(textFilter, keyCode);
    }
    // after a selection we have to capture the new value and add it to the existing text
    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);
    }
}
