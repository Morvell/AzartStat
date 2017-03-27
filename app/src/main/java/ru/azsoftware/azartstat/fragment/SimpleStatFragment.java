package ru.azsoftware.azartstat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.azsoftware.azartstat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleStatFragment extends Fragment  {


    public SimpleStatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple_stat, container, false);


        return view;
    }

}
