package ru.azsoftware.azartstat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.azsoftware.azartstat.data.BetContract;
import ru.azsoftware.azartstat.data.BetDBHelper;

public class SettingsActivity extends AppCompatActivity {

    Button buttonDeleteDB;
    Button buttonEditBank;
    EditText editTextEditBank;
    BetDBHelper mDbHelper;

    private SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_BANK = "bank";
    public static final String APP_PREFERENCES_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        buttonDeleteDB = (Button) findViewById(R.id.buttonDeleteDB);
        buttonEditBank = (Button) findViewById(R.id.buttonEditBank);
        editTextEditBank = (EditText) findViewById(R.id.editTextEditBank);
        try {
            int bank = mSettings.getInt(APP_PREFERENCES_BANK,0);
            editTextEditBank.setText(String.valueOf(bank));
        } catch (Exception e) {}

        buttonEditBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSettings.edit();
                String bank = editTextEditBank.getText().toString();
                editor.putInt(APP_PREFERENCES_BANK,Integer.valueOf(bank));
                editor.putString(APP_PREFERENCES_DATE,"0.0.0");
                editor.apply();
            }
        });

        buttonDeleteDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BetDBHelper betDBHelper = new BetDBHelper(getApplicationContext());
                SQLiteDatabase db = betDBHelper.getWritableDatabase();
                int deleatRow = db.delete(BetContract.BetEntry.TABLE_NAME, null,null);
                Toast.makeText(getApplication(),"Данные удалены в количестве: " + deleatRow + "шт.", Toast.LENGTH_SHORT).show();
            }
        });

        mDbHelper = new BetDBHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
