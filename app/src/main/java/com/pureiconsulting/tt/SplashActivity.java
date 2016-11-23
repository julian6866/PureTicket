package com.pureiconsulting.tt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by julianzhu on 11/15/15.
 */
public class SplashActivity extends Activity implements LoadingTask.LoadingTaskFinishedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        new LoadingTask(this.getApplicationContext(), this).execute();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

    }

    @Override
    public void onTaskFinished() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);

        Intent pullServiceIntent = new Intent(this, PullService.class);
        startService(pullServiceIntent);

        finish();
    }

}
