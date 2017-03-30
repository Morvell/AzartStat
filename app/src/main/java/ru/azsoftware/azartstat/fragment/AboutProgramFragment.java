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
public class AboutProgramFragment extends Fragment {


    public AboutProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_program, container, false);
    }

}
