package ru.azsoftware.azartstat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.azsoftware.azartstat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GrafStatFragment extends Fragment {


    public GrafStatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graf_stat, container, false);

        return view;
    }

}
