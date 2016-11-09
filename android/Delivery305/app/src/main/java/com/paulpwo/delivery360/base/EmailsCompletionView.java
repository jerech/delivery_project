package com.paulpwo.delivery360.base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulpwo.delivery360.R;
import com.tokenautocomplete.TokenCompleteTextView;

import static java.security.AccessController.getContext;

/**
 * Created by jeremias on 8/11/16.
 */

public class EmailsCompletionView extends TokenCompleteTextView<String> {
    public EmailsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(String email) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.email_token, (ViewGroup) getParent(), false);
        view.setText(email);

        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        return completionText;
    }
}