package ru.azsoftware.azartstat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.azsoftware.azartstat.data.BetContract;
import ru.azsoftware.azartstat.data.BetDBHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    EditText editTextDate, editTextProfit, editTextBank;
    Button buttonToday, buttonYesterday, buttonSave, buttonNext;
    TextView textViewBank;

    private SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_BANK = "bank";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle("AppCompatDialog");
        builder.setMessage("Lorem ipsum dolor...");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Maybee", null);
        builder.show();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextProfit = (EditText) findViewById(R.id.editTextSum);
        editTextBank = (EditText) findViewById(R.id.editTextBank);
        buttonToday = (Button) findViewById(R.id.buttonToday);
        buttonYesterday = (Button) findViewById(R.id.buttonYesterday);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        textViewBank = (TextView) findViewById(R.id.textViewBank);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textViewBank.setVisibility(View.VISIBLE);
                editTextBank.setVisibility(View.VISIBLE);
                int bank = 0;
                try{ bank = mSettings.getInt(APP_PREFERENCES_BANK,0);}catch (Exception e) {}
                bank += Integer.valueOf(editTextProfit.getText().toString());
                editTextBank.setText(String.valueOf(bank));


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

        //long date = System.currentTimeMillis();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String dateString = sdf.format(date.getTimeInMillis());
        editTextDate.setText(dateString);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {


        } else if (id == R.id.nav_statistic) {

            Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about_program) {


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void insertDate() {

        // Gets the database in write mode
        BetDBHelper betDBHelper = new BetDBHelper(this);
        SQLiteDatabase db = betDBHelper.getWritableDatabase();

        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о госте является значениями ключей
        String date = editTextDate.getText().toString();
        int profit = Integer.parseInt(editTextProfit.getText().toString());
        int bank = Integer.parseInt(editTextBank.getText().toString());
        ContentValues values = new ContentValues();
        values.put(BetContract.BetEntry.COLUMN_DATE, date);
        values.put(BetContract.BetEntry.COLUMN_BANK,bank);
        values.put(BetContract.BetEntry.COLUMN_PROFIT, profit);


        long newRowId = db.insert(BetContract.BetEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Данные за " + date + " уже существуют", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Данные за " + date + " заведёны", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_BANK,bank);
            editor.apply();
        }
    }
}
