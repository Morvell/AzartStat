package ru.azsoftware.azartstat.fragment;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.azsoftware.azartstat.R;
import ru.azsoftware.azartstat.data.BetContract;
import ru.azsoftware.azartstat.data.BetDBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetingsFragment extends Fragment {

    Button buttonDeleteDB;
    Button button;
    BetDBHelper mDbHelper;


    public SetingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setings, container, false);

        buttonDeleteDB = (Button) view.findViewById(R.id.buttonDeleteDB);
        button = (Button) view.findViewById(R.id.button);

        buttonDeleteDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BetDBHelper betDBHelper = new BetDBHelper(getActivity().getApplicationContext());
                SQLiteDatabase db = betDBHelper.getWritableDatabase();
                int deleatRow = db.delete(BetContract.BetEntry.TABLE_NAME, null,null);
                Toast.makeText(view.getContext(),"Данные удалены" + deleatRow, Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatabaseInfo();
            }
        });

        mDbHelper = new BetDBHelper(view.getContext());

        return view;
    }

    private void displayDatabaseInfo() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {

                BetContract.BetEntry.COLUMN_DATE,
                BetContract.BetEntry.COLUMN_PROFIT };

        // Делаем запрос
        Cursor cursor = db.query(
                BetContract.BetEntry.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        TextView displayTextView = (TextView) getActivity().findViewById(R.id.text_view_info);


        try {
            displayTextView.setText("Таблица содержит " + cursor.getCount() + " данные.\n\n");
            displayTextView.append(
                    BetContract.BetEntry.COLUMN_DATE + " - " +

                    BetContract.BetEntry.COLUMN_PROFIT + "\n");

            // Узнаем индекс каждого столбца

            int dateColumnIndex = cursor.getColumnIndex(BetContract.BetEntry.COLUMN_DATE);
            int profitColumnIndex = cursor.getColumnIndex(BetContract.BetEntry.COLUMN_PROFIT);


            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа

                String currentName = cursor.getString(dateColumnIndex);
                String currentCity = cursor.getString(profitColumnIndex);

                // Выводим значения каждого столбца
                displayTextView.append(("\n" +
                        currentName + " - " +
                        currentCity));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }
}
