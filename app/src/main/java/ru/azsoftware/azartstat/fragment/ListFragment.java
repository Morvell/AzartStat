package ru.azsoftware.azartstat.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import ru.azsoftware.azartstat.ListViewAdapter;
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
public class ListFragment extends Fragment {

    private ArrayList<HashMap<String, String>> list;
    SQLiteDatabase db;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);


        ListView listView = (ListView) view.findViewById(R.id.listView1);

        list = DateGenerat();



        ListViewAdapter adapter = new ListViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int pos = position + 1;
                Toast.makeText(getActivity(), Integer.toString(pos) + " Clicked", Toast.LENGTH_SHORT).show();
            }

        });
    return view;
    }

    public ArrayList<HashMap<String, String>> DateGenerat() {

        BetDBHelper betDBHelper = new BetDBHelper(getActivity());
        db = betDBHelper.getWritableDatabase();

        String query = "SELECT " + BetContract.BetEntry.COLUMN_DATE+", "+ BetContract.BetEntry.COLUMN_BANK+", "+ BetContract.BetEntry.COLUMN_PROFIT
                +" FROM " + BetContract.BetEntry.TABLE_NAME +" ORDER BY " + BetContract.BetEntry.COLUMN_DATE + " DESC";

        Cursor cursor = db.rawQuery(query,null);
        int bank = 0;
        String date = "";
        int profit = 0;

        int count = cursor.getCount();
        ArrayList<HashMap<String, String>> list2 = new ArrayList<>();

        int i = 0;


        while (cursor.moveToNext()) {
            date = cursor.getString(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_DATE));
            profit = cursor.getInt(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_PROFIT));
            bank = cursor.getInt(cursor
                    .getColumnIndex(BetContract.BetEntry.COLUMN_BANK));



            HashMap<String, String> temp = new HashMap<>();
            temp.put(FIRST_COLUMN, String.valueOf(i+1));
            temp.put(SECOND_COLUMN, date);
            temp.put(THIRD_COLUMN, String.valueOf(profit));
            temp.put(FOURTH_COLUMN, String.valueOf(bank));
            list2.add(temp);
            i+=1;
        }

        cursor.close();

        return list2;
    }
}
