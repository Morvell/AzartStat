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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import ru.azsoftware.azartstat.R;
import ru.azsoftware.azartstat.data.BetContract.BetEntry;
import ru.azsoftware.azartstat.data.BetDBHelper;

public class MainFragment extends Fragment {

    EditText editTextDate, editTextBankOrProfitUp, editTextBankOrProfitDown;
    Button buttonToday, buttonYesterday, buttonSave, buttonNext;
    TextView textViewBankOrProfit;
    Spinner spinner;

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
        editTextBankOrProfitUp = (EditText) view.findViewById(R.id.editTextBankOrProfitUp);
        editTextBankOrProfitDown = (EditText) view.findViewById(R.id.editTextBankOrProfitDown);
        buttonToday = (Button) view.findViewById(R.id.buttonToday);
        buttonYesterday = (Button) view.findViewById(R.id.buttonYesterday);
        buttonSave = (Button) view.findViewById(R.id.buttonSave);
        buttonNext = (Button) view.findViewById(R.id.buttonNext);
        textViewBankOrProfit = (TextView) view.findViewById(R.id.textViewBankOrProfit);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textViewBankOrProfit.setVisibility(View.VISIBLE);
                editTextBankOrProfitDown.setVisibility(View.VISIBLE);
                buttonSave.setVisibility(View.VISIBLE);
                buttonNext.setVisibility(View.INVISIBLE);

                String variant = spinner.getSelectedItem().toString();

                if (Objects.equals(variant, "Прибыль")) {

                    textViewBankOrProfit.setText("Банк");
                    int bank = 0;
                    try{ bank = mSettings.getInt(APP_PREFERENCES_BANK,0);}catch (Exception e) {}
                    bank += Integer.valueOf(editTextBankOrProfitUp.getText().toString());
                    editTextBankOrProfitDown.setText(String.valueOf(bank));
                } else {
                    textViewBankOrProfit.setText("Прибыль");
                    int bank = 0;
                    int profit = 0;
                    try{ bank = mSettings.getInt(APP_PREFERENCES_BANK,0);}catch (Exception e) {}
                    profit = Integer.valueOf(editTextBankOrProfitUp.getText().toString()) - bank;
                    editTextBankOrProfitDown.setText(String.valueOf(profit));
                }

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
        int profit = Integer.parseInt(editTextBankOrProfitUp.getText().toString());
        int bank = Integer.parseInt(editTextBankOrProfitDown.getText().toString());
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
        builder.setNeutralButton("Суммировать", ChangeDB);
        builder.show();
    }

    DialogInterface.OnClickListener UpdateDB = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ContentValues values = new ContentValues();
            values.put(BetEntry.COLUMN_PROFIT, Integer.valueOf(editTextBankOrProfitUp.getText().toString()));
            values.put(BetEntry.COLUMN_BANK, Integer.valueOf(editTextBankOrProfitDown.getText().toString()));

            db.update(BetEntry.TABLE_NAME,
                    values,
                    BetEntry.COLUMN_DATE + "= ?", new String[]{editTextDate.getText().toString()});

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_BANK,Integer.valueOf(editTextBankOrProfitDown.getText().toString()));
            editor.apply();
            Toast.makeText(getActivity(), "Данные заменены.", Toast.LENGTH_SHORT).show();

        }
    };

    DialogInterface.OnClickListener ChangeDB = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ContentValues values = new ContentValues();
            String query = "SELECT " + BetEntry.COLUMN_PROFIT+", "+ BetEntry.COLUMN_BANK
                         +" FROM " + BetEntry.TABLE_NAME
                         +" WHERE "+ BetEntry.COLUMN_DATE+"="+editTextDate.getText().toString();
            Cursor cursor = db.rawQuery(query,null);
            int profit =0, bank = 0;
            while (cursor.moveToNext()) {
                profit = cursor.getInt(cursor
                        .getColumnIndex(BetEntry.COLUMN_PROFIT));
                bank = cursor.getInt(cursor
                        .getColumnIndex(BetEntry.COLUMN_BANK));
            }

            values.put(BetEntry.COLUMN_PROFIT, Integer.valueOf(editTextBankOrProfitUp.getText().toString()) + profit);
            values.put(BetEntry.COLUMN_BANK, bank + profit);

            db.update(BetEntry.TABLE_NAME,
                    values,
                    BetEntry.COLUMN_DATE + "= ?", new String[]{editTextDate.getText().toString()});

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_BANK, bank + profit);
            editor.apply();
            cursor.close();
            Toast.makeText(getActivity(), "Данные суммированы.", Toast.LENGTH_SHORT).show();
        }
    };

}





