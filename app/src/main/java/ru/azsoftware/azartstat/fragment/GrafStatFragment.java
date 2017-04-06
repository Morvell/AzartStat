package ru.azsoftware.azartstat.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.azsoftware.azartstat.R;
import ru.azsoftware.azartstat.data.BetContract;
import ru.azsoftware.azartstat.data.BetDBHelper;

import static android.R.attr.format;

public class GrafStatFragment extends Fragment {

    SQLiteDatabase db;

    public GrafStatFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graf_stat, container, false);

        BetDBHelper betDBHelper = new BetDBHelper(getActivity());
        db = betDBHelper.getWritableDatabase();

        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = null;
        try {
            series = new LineGraphSeries<>(DataPointGenerat());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        graph.addSeries(series);

        return view;
    }

    public DataPoint[] DataPointGenerat() throws ParseException {

        String query = "SELECT " + BetContract.BetEntry.COLUMN_DATE+", "+ BetContract.BetEntry.COLUMN_BANK
                +" FROM " + BetContract.BetEntry.TABLE_NAME + " ORDER BY " + BetContract.BetEntry.COLUMN_DATE + " ASC";

        Cursor cursor = db.rawQuery(query,null);
        int bank = 0;
        int count = cursor.getCount();
        DataPoint[] values = new DataPoint[count];

        int i = 0;

        while (cursor.moveToNext()) {
            bank = cursor.getInt(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_BANK));
            DataPoint v = new DataPoint(i+1, bank);
            values[i] = v;
            i+=1;
        }

        cursor.close();

        return values;
    }

}
