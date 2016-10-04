package com.paulpwo.delivery305.base;

import android.support.annotation.Keep;
import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;
import com.paulpwo.delivery305.service.serviceDefault;

public abstract class BaseFragmentSpice extends Fragment {
    private final SpiceManager spiceManager = new SpiceManager(serviceDefault.class );

    public BaseFragmentSpice() {
        // Required empty public constructor
    }
   @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        spiceManager.start(getContext());
        super.onStart();
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
