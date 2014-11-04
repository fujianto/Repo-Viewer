package com.naisinpo.fujianto.repoviewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fujianto on 28/10/14.
 */
public class GithubRepoParser {
    private final String LOG_TAG = GithubRepoParser.class.getSimpleName();
    private String jsonString;

    public GithubRepoParser(String jsonString){
        this.jsonString = jsonString;
    }

    public ArrayList<ArrayList<String>> getListRepoDataFromJSON() throws JSONException{
        ArrayList<ArrayList<String>>  results = new ArrayList<ArrayList<String>> ();

        JSONArray repoArray = new JSONArray(jsonString);
        final String REPO_DATA = "name";
        final String REPO_HTML_URL = "html_url";
        final String REPO_DESC = "description";
        final String REPO_CREATE_AT = "created_at";
        final String REPO_UPDATED_AT = "updated_at";
        final String REPO_HOMEPAGE = "homepage";
        final String REPO_LANGUAGE = "language";

        ArrayList<String> listRepoName = new ArrayList<String>();
        ArrayList<String> listRepoURL = new ArrayList<String>();
        ArrayList<String> listRepoDesc = new ArrayList<String>();
        ArrayList<String> listRepoLang = new ArrayList<String>();

        for(int i = 0; i < repoArray.length(); i++){
            JSONObject jsonObjRepo = repoArray.getJSONObject(i);
            String repoName = jsonObjRepo.getString(REPO_DATA);
            String repoURL = jsonObjRepo.getString(REPO_HTML_URL);
            String repoDesc = jsonObjRepo.getString(REPO_DESC);
            String repoCreated = jsonObjRepo.getString(REPO_CREATE_AT);
            String repoUpdated = jsonObjRepo.getString(REPO_UPDATED_AT);
            String repoHomepage = jsonObjRepo.getString(REPO_HOMEPAGE);
            String repoLang = jsonObjRepo.getString(REPO_LANGUAGE);

            // Add to Array List for each value from JSON
            listRepoName.add(i, repoName);
            listRepoURL.add(i, repoURL);
            listRepoDesc.add(i, repoDesc);
            listRepoLang.add(i, repoLang);
        }

        // Each JSON Array List in a single ArrayList result
        results.add(0, listRepoName);
        results.add(1, listRepoURL);
        results.add(2, listRepoDesc);
        results.add(3, listRepoLang);


        return results;
    }

    public String getRepoName(int repoIndex) throws JSONException{
        String repoName;

        JSONArray jsonArrRepo = new JSONArray(jsonString);
        JSONObject jsonObjRepo = jsonArrRepo.getJSONObject(repoIndex);
        repoName = jsonObjRepo.getString("name");

        return repoName;
    }

}
