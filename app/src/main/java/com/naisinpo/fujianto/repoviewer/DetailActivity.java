package com.naisinpo.fujianto.repoviewer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;


public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true); //For ICS
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {
        private TextView txvRepoTitle, txvRepoURL, txvRepoDesc;
        final static String LOG_TAG = DetailFragment.class.getSimpleName();
        private String repoDetailURL, repoDetailTitle, repoDetailDesc;
        private ShareActionProvider mShareActionProvider;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.fragmentdetail,menu);
            mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
            if(mShareActionProvider != null){
                mShareActionProvider.setShareIntent(createShareRepo());
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if(id == android.R.id.home){
                getActivity().finish();
            }

            return true;
        }

        private Intent createShareRepo() {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, repoDetailTitle +" - "+ repoDetailURL);

            return intent;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            repoDetailTitle = intent.getStringExtra(Intent.EXTRA_TEXT+"REPONAME");
            repoDetailURL = intent.getStringExtra(Intent.EXTRA_TEXT+"REPOURL");
            repoDetailDesc = intent.getStringExtra(Intent.EXTRA_TEXT+"REPODESC");

            txvRepoTitle = (TextView) rootView.findViewById(R.id.textViewDetailTitle);
            txvRepoURL = (TextView) rootView.findViewById(R.id.textViewDetailURL);
            txvRepoDesc = (TextView) rootView.findViewById(R.id.textViewDetailDesc);
            txvRepoTitle.setText(repoDetailTitle);
            txvRepoURL.setText(repoDetailURL);
            txvRepoDesc.setText(repoDetailDesc);

            return rootView;
        }
    }
}
