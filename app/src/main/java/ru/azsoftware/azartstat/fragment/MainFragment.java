package ru.azsoftware.azartstat.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import ru.azsoftware.azartstat.R;
import ru.azsoftware.azartstat.data.BetContract;
import ru.azsoftware.azartstat.data.BetContract.BetEntry;
import ru.azsoftware.azartstat.data.BetDBHelper;

import static ru.azsoftware.azartstat.Constants.FIRST_COLUMN;
import static ru.azsoftware.azartstat.Constants.FOURTH_COLUMN;
import static ru.azsoftware.azartstat.Constants.SECOND_COLUMN;
import static ru.azsoftware.azartstat.Constants.THIRD_COLUMN;

public class MainFragment extends Fragment {

    EditText editTextDate, editTextBank, editTextProfit;
    Button buttonToday, buttonYesterday, buttonSave;
    Button search;

    SQLiteDatabase db;

    private SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_BANK = "bank";

    public MainFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =  inflater.inflate(R.layout.fragment_main, container, false);

        BetDBHelper betDBHelper = new BetDBHelper(getActivity());
        db = betDBHelper.getWritableDatabase();

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        editTextDate = (EditText) view.findViewById(R.id.editTextDate);
        editTextBank = (EditText) view.findViewById(R.id.editTextBank);
        editTextProfit = (EditText) view.findViewById(R.id.editTextProfit);
        buttonToday = (Button) view.findViewById(R.id.buttonToday);
        buttonYesterday = (Button) view.findViewById(R.id.buttonYesterday);
        buttonSave = (Button) view.findViewById(R.id.buttonSave);
        search = (Button) view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = editTextDate.getText().toString();
                
                String selection = BetEntry.COLUMN_DATE + "= ?";
                String[] selectionArgs = {date};

                String query = "SELECT " + BetEntry.COLUMN_BANK + ", " + BetEntry.COLUMN_PROFIT
                        + " FROM " + BetEntry.TABLE_NAME + " WHERE " +selection ;

                Cursor cursor = db.rawQuery(query, selectionArgs );

                int profit, bank;

                if (cursor.moveToNext()) {
                    profit = cursor.getInt(cursor
                            .getColumnIndex(BetEntry.COLUMN_PROFIT));
                    bank = cursor.getInt(cursor
                            .getColumnIndex(BetEntry.COLUMN_BANK));

                    editTextBank.setText(String.valueOf(bank));
                    editTextProfit.setText(String.valueOf(profit));
                }
                else {
                    Toast.makeText(getActivity(), "Данных за этот день не найдено",Toast.LENGTH_SHORT).show();
                }

                cursor.close();
            }
        });



        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, 0);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                String dateString = sdf.format(date.getTimeInMillis());
                editTextDate.setText(dateString);
            }
        });

        buttonYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, -1);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                String dateString = sdf.format(date.getTimeInMillis());
                editTextDate.setText(dateString);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDate();
            }
        });

        editTextDate.setSelection(editTextDate.length());

        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String dateString = sdf.format(date.getTimeInMillis());
        editTextDate.setText(dateString);

        return view;
    }

    private void insertDate() {

        String date = editTextDate.getText().toString();
        int profit, bank;

        bank = Integer.parseInt(editTextBank.getText().toString());
        profit = Integer.parseInt(editTextProfit.getText().toString());

        ContentValues values = new ContentValues();
        String day, month, age;
        String[] splitDate = date.split("[.]");
        day = splitDate[0];
        month = splitDate[1];
        age = splitDate[2];
        String revertDate = age + "." + month + "." + day;

        values.put(BetEntry.COLUMN_DATE, date);
        values.put(BetEntry.COLUMN_REVERT_DATE, revertDate);
        values.put(BetEntry.COLUMN_BANK, bank);
        values.put(BetEntry.COLUMN_PROFIT, profit);

        long newRowId = db.insert(BetEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(getActivity(), "Данные за " + date + " уже существуют", Toast.LENGTH_SHORT).show();
            MyAlertDialog();

        } else {
            Toast.makeText(getActivity(), "Данные за " + date + " заведёны", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_BANK,bank);
            editor.apply();
        }
    }

    private void MyAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setMessage("Данные за этот день уже были заведены ранее. Заменить данные?");
        builder.setPositiveButton("Да", UpdateDB);
        builder.setNegativeButton("Нет", null);
        builder.show();
    }

    DialogInterface.OnClickListener UpdateDB = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            int profit, bank;

            bank = Integer.parseInt(editTextBank.getText().toString());
            profit = Integer.parseInt(editTextProfit.getText().toString());

            ContentValues values = new ContentValues();
            values.put(BetEntry.COLUMN_PROFIT, profit);
            values.put(BetEntry.COLUMN_BANK, bank);

            db.update(BetEntry.TABLE_NAME,
                    values,
                    BetEntry.COLUMN_DATE + "= ?", new String[]{editTextDate.getText().toString()});

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_BANK, bank);
            editor.apply();
            Toast.makeText(getActivity(), "Данные заменены.", Toast.LENGTH_SHORT).show();

        }
    };



}





