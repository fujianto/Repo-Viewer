package com.naisinpo.fujianto.repoviewer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, " ~~~~~~~~~~~~~~~~~~~~~~~ onCreate");
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new RepoViewerFragment())
                    .commit();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, " ~~~~~~~~~~~~~~~~~~~~~~~ onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~ onStop");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~ onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~ onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~ onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_browser) {
            openRepoURL();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openRepoURL(){
        final String GITHUB_URL = "https://github.com/";
//        final String GITHUB_USERNAME = "fujianto";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String githubUsername = prefs.getString(
                getString(R.string.pref_username_key), null);

        RepoViewerFragment fragment = new RepoViewerFragment();
        if(githubUsername == null || githubUsername.isEmpty()){
            fragment.setGitHubUserName();
        }

        Uri builtUri = Uri.parse(GITHUB_URL).buildUpon().appendPath(githubUsername).build();
        Intent intentBrowser = new Intent(Intent.ACTION_VIEW);
        intentBrowser.setData(Uri.parse(builtUri.toString()));
        //Check if intent could be opened with any installed apps.
        if(intentBrowser.resolveActivity(getPackageManager()) != null){
            startActivity(intentBrowser);
        }
    }

}
