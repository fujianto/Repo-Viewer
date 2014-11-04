package com.naisinpo.fujianto.repoviewer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A RepoViewerFragment fragment containing a List view.
 */
public class RepoViewerFragment extends Fragment{
    private ListView lvRepo;
    private ArrayAdapter<String> repoAdapter;
    private final String LOG_TAG = RepoViewerFragment.class.getSimpleName();
    private String[] dummyRepo = {};
    private ArrayList<String> mlistRepoDesc = new ArrayList<String>();
    private ArrayList<String> mlistRepoUrl = new ArrayList<String>();
    private SharedPreferences prefs;
    private String githubUsername;

    public RepoViewerFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        githubUsername = prefs.getString(getString(R.string.pref_username_key), null);
        if(githubUsername == null || githubUsername.isEmpty()){
            setGitHubUserName();
        } else{
            GithubRepoTask fetchData = new GithubRepoTask();
            fetchData.execute(githubUsername);  //Github Username
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lvRepo = (ListView) rootView.findViewById(R.id.listViewRepo);
        List<String> dummyRepoList = new ArrayList<String>(Arrays.asList(dummyRepo));
        repoAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dummyRepoList);
        lvRepo.setAdapter(repoAdapter);

        lvRepo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String repoName = repoAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT+"REPONAME", repoName);
                intent.putExtra(Intent.EXTRA_TEXT + "REPOURL", mlistRepoUrl.get(position));
                intent.putExtra(Intent.EXTRA_TEXT+"REPODESC", mlistRepoDesc.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void setGitHubUserName(){
        AlertDialog.Builder alertUsername = new AlertDialog.Builder(getActivity());
        alertUsername.setTitle("Github Username");
        alertUsername.setMessage("Please enter your Github Username.");

        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());
        input.setSingleLine(true);
        alertUsername.setView(input);

        alertUsername.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.pref_username_key), value);
                editor.commit();

                githubUsername = prefs.getString(getString(R.string.pref_username_key), null);
                GithubRepoTask fetchData = new GithubRepoTask();
                fetchData.execute(githubUsername);  //Github Username
            }
        });

        alertUsername.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
//                System.exit(0);
            }
        });

        alertUsername.show();
    }

    public class GithubRepoTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>> > {
        private ProgressDialog progressDialog;
        private final String LOG_TAG = GithubRepoTask.class.getSimpleName();

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... params) {
            final String BASE_URL = "https://api.github.com/";
            final String USER_PARAM = "users";
            final String OWNER_PARAM = params[0];
            final String REPO_PARAM = "repos";
            final String apiURL = BASE_URL+USER_PARAM+"/"+OWNER_PARAM+"/"+REPO_PARAM;

            String jsonString = getJSONFromAPI(apiURL);
            GithubRepoParser parsedData = new GithubRepoParser(jsonString);

            try {
                ArrayList<ArrayList<String>> listData = parsedData.getListRepoDataFromJSON();
                return listData;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param results The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> results) {
            super.onPostExecute(results);
            progressDialog.dismiss();

            if(results!= null){
                repoAdapter.clear();
                ArrayList<String> listRepoName = results.get(0);
                ArrayList<String> listRepoURL = results.get(1);
                ArrayList<String> listRepoDesc = results.get(2);
                ArrayList<String> listRepoLang = results.get(3);

                mlistRepoUrl = listRepoURL;
                mlistRepoDesc = listRepoDesc;

                for(String listValue : listRepoName){
                    repoAdapter.add(listValue);
                }
            }
        }

        public String getJSONFromAPI(String apiUrl){
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonString = null;

            try {
                URL url = new URL(apiUrl.toString());

                // Create the request to API, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }

                jsonString = buffer.toString();

            } catch (IOException e  ){
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return jsonString;
        }
    }

}