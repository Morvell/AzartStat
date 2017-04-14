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
import java.util.ArrayList;
import java.util.HashMap;

import ru.azsoftware.azartstat.R;
import ru.azsoftware.azartstat.data.BetContract;
import ru.azsoftware.azartstat.data.BetDBHelper;

import static ru.azsoftware.azartstat.Constants.FIRST_COLUMN;
import static ru.azsoftware.azartstat.Constants.FOURTH_COLUMN;
import static ru.azsoftware.azartstat.Constants.SECOND_COLUMN;
import static ru.azsoftware.azartstat.Constants.THIRD_COLUMN;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleStatFragment extends Fragment  {

    TextView textViewBank, textViewMaxBank, textViewMinBank, textViewFivePercent,
            textViewAverageProfit, textViewMaxProfit, textViewMinProfit, textViewExpectedProfit;
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

        textViewBank = (TextView) view.findViewById(R.id.textViewBankStat);
        textViewMaxBank = (TextView) view.findViewById(R.id.textViewMaxBankStat);
        textViewMinBank = (TextView) view.findViewById(R.id.textViewMinBankStat);
        textViewAverageProfit = (TextView) view.findViewById(R.id.textViewAverageProfitStat);
        textViewMaxProfit = (TextView) view.findViewById(R.id.textViewMaxProfitStat);
        textViewMinProfit = (TextView) view.findViewById(R.id.textViewMinProfitStat);
        textViewExpectedProfit = (TextView) view.findViewById(R.id.textViewExpectedProfitStat);
        textViewFivePercent = (TextView) view.findViewById(R.id.textVievFivePercent);


        String query = "SELECT " + BetContract.BetEntry.COLUMN_BANK + ", " + BetContract.BetEntry.COLUMN_PROFIT
                + " FROM " + BetContract.BetEntry.TABLE_NAME + " ORDER BY " + BetContract.BetEntry.COLUMN_REVERT_DATE + " DESC";

        Cursor cursor = db.rawQuery(query,null);
        int profit = 0;
        int bank;
        int maxBank = 0;
        int minBank = 999999999;
        int maxProfit = 0;
        int minProfit = 99999999;
        int profitFromDB;

        while (cursor.moveToNext()) {
            profitFromDB = cursor.getInt(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_PROFIT));

            bank = cursor.getInt(cursor.getColumnIndex(BetContract.BetEntry.COLUMN_BANK));

            if (maxBank < bank) maxBank = bank;
            if (minBank > bank) minBank = bank;

            profit += profitFromDB;

            if (maxProfit < profitFromDB) maxProfit = profitFromDB;
            if (minProfit > profitFromDB) minProfit = profitFromDB;
        }

        cursor.moveToFirst();
        textViewBank.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(BetContract.BetEntry.COLUMN_BANK))));
        textViewFivePercent.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(BetContract.BetEntry.COLUMN_BANK)) * 0.05));
        textViewMaxBank.setText(String.valueOf(maxBank));
        textViewMinBank.setText(String.valueOf(minBank));
        textViewAverageProfit.setText(String.valueOf(profit/cursor.getCount()));
        textViewMinProfit.setText(String.valueOf(minProfit));
        textViewMaxProfit.setText(String.valueOf(maxProfit));
        textViewExpectedProfit.setText(String.valueOf(profit/cursor.getCount() * 30));


        cursor.close();




        return view;
    }

}
