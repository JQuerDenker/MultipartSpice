package com.example.MultipartSpice;

import android.app.Activity;
import android.os.Bundle;
import com.octo.android.robospice.SpiceManager;
import com.example.MultipartSpice.service.BaseSpiceService;

/**
 * Project: MultipartSpice
 * Package: com.example.MultipartSpice
 * User: Nelson Sachse
 */
public class BaseActivity extends Activity{

    public SpiceManager mSpiceManager = new SpiceManager(BaseSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        mSpiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }


}
