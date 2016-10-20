package com.paulpwo.delivery360.utils;

import android.content.Context;
import android.support.annotation.Keep;

/**
 * Created by paulpwo on 10/7/16.
 */
@Keep
public class HelpersMenu {
    private static HelpersMenu ourInstance = new HelpersMenu();

    private Context c;
    private String first_name;
    private String last_name;
    private String phone;

    public static HelpersMenu getInstance() {
        return ourInstance;
    }

    private HelpersMenu() {
    }








}
