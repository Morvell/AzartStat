package ru.azsoftware.azartstat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.azsoftware.azartstat.R;

public class AboutProgramFragment extends Fragment {


    public AboutProgramFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_program, container, false);
    }

}
