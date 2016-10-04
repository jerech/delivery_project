package com.paulpwo.delivery305.interfaces;

import android.support.annotation.Keep;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by paulpwo on 12/7/16.
 */

public interface publicOKhttp {

    public  void onFailureInMainThread(Call call, IOException e);
    public  void onResponseInMainThread(Call call, Response response) throws IOException;

}