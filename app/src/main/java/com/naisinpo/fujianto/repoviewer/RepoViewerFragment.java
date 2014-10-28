package com.naisinpo.fujianto.repoviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A RepoViewerFragment fragment containing a List view.
 */
public class RepoViewerFragment extends Fragment {
    private ListView lvRepo;
    private ArrayAdapter<String> repoAdapter;
    private String[] dummyRepo = {"Placeholder 1", "Placeholder 2", "Placeholder 3"};

    public RepoViewerFragment() {
    }

    /**
     * Called to do initial creation of a fragment.
     * @param savedInstanceState If the fragment is being re-created from
     *a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lvRepo = (ListView) rootView.findViewById(R.id.listViewRepo);
        List<String> dummyRepoList = new ArrayList<String>(Arrays.asList(dummyRepo));
        repoAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dummyRepoList);
        lvRepo.setAdapter(repoAdapter);

        // Fetch data from APi
        GithubRepoTask fetchData = new GithubRepoTask(getActivity(), repoAdapter);
        fetchData.execute("fujianto");  //Github Username

        return rootView;
    }
}