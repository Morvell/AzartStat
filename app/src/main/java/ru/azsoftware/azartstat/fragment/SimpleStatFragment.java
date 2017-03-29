package ru.azsoftware.azartstat.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;

import ru.azsoftware.azartstat.R;
import ru.azsoftware.azartstat.data.BetContract;
import ru.azsoftware.azartstat.data.BetDBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleStatFragment extends Fragment  {

    TextView multiline;
    SQLiteDatabase db;


    public SimpleStatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple_stat, container, false);

        BetDBHelper betDBHelper = new BetDBHelper(getActivity());
        db = betDBHelper.getWritableDatabase();

        multiline = (TextView) view.findViewById(R.id.multiline);

        for (String i:DateGenerat())
        {
            multiline.append(i);
        }


        return view;
    }
    public String[] DateGenerat() {

        String query = "SELECT " + BetContract.BetEntry.COLUMN_DATE+", "+ BetContract.BetEntry.COLUMN_BANK+", "+ BetContract.BetEntry.COLUMN_PROFIT
                +" FROM " + BetContract.BetEntry.TABLE_NAME +" ORDER BY " + BetContract.BetEntry.COLUMN_DATE + " DESC";

        Cursor cursor = db.rawQuery(query,null);
        int bank = 0;
        String date = "";
        int profit = 0;

        int count = cursor.getCount();
        String [] values = new String[count];

        int i = 0;


        while (cursor.moveToNext()) {
            date = cursor.getString(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_DATE));
            profit = cursor.getInt(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_PROFIT));
            bank = cursor.getInt(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_BANK));



            String v = date + "|"+ profit + "|" + bank+"\n";
            values[i] = v;
            i+=1;
        }

        cursor.close();

        return values;
    }
}
