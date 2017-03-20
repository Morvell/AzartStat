package ru.azsoftware.azartstat.fragment;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.azsoftware.azartstat.R;
import ru.azsoftware.azartstat.data.BetDBHelper;
import ru.azsoftware.azartstat.data.BetContract.BetEntry;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    EditText editTextDate, editTextSum;
    Button buttonToday, buttonYesterday, buttonSave;




    public MainFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        editTextDate = (EditText) view.findViewById(R.id.editTextDate);
        editTextSum = (EditText) view.findViewById(R.id.editTextSum);
        buttonToday = (Button) view.findViewById(R.id.buttonToday);
        buttonYesterday = (Button) view.findViewById(R.id.buttonYesterday);
        buttonSave = (Button) view.findViewById(R.id.buttonSave);




        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE,0);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                String dateString = sdf.format(date.getTimeInMillis());
                editTextDate.setText(dateString);
            }
        });

        buttonYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE,-1);

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

        //long date = System.currentTimeMillis();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String dateString = sdf.format(date.getTimeInMillis());
        editTextDate.setText(dateString);


        return view;
    }

    private void insertDate() {

        // Gets the database in write mode
        BetDBHelper betDBHelper = new BetDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = betDBHelper.getWritableDatabase();

        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о госте является значениями ключей
        String date = editTextDate.getText().toString();
        int profit = Integer.parseInt(editTextSum.getText().toString());
        ContentValues values = new ContentValues();
        values.put(BetEntry.COLUMN_DATE, date);
        values.put(BetEntry.COLUMN_PROFIT, profit);


        long newRowId = db.insert(BetEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(getActivity().getApplicationContext(), "Данные за "+ date + " уже существуют" , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Данные за "+ date + " заведёны" , Toast.LENGTH_SHORT).show();
        }
    }


    }





