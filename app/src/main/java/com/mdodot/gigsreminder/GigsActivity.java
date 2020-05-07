package com.mdodot.gigsreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class GigsActivity extends AppCompatActivity {

    ArrayList<GigModel> gigsList;
    ListView listView;
    DBHelper dbHelper;
    DataManager dataManager;
    Intent intent;
    SQLiteDatabase db;
    private static GigsAdapter adapter;
    static final int REQUEST_CODE = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId()) {
                case R.id.add_event:
                    intent = new Intent(this, AddEvent.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    return(true);
                case R.id.venues:
                    intent = new Intent(this, VenuesActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    return(true);
                case R.id.settings:
                    Snackbar.make(this.findViewById(android.R.id.content), "c", 2000).show();
                    return(true);
            }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        loadData();
    }

    public void loadData() {
        listView = (ListView)findViewById(R.id.list);
        dataManager = DataManager.getInstance();
        dataManager.loadFromDatabase(dbHelper);
        dataManager.loadGigsFromDatabase(dataManager.gigsCursor);
        gigsList = dataManager.gigsList;
        adapter = new GigsAdapter(gigsList, this);
        listView.setAdapter(adapter);
    }

    protected  void deleteGig(int gigId) {
        db = dbHelper.getWritableDatabase();
        dbHelper.deleteGig(db, gigId);
        loadData();
        Snackbar.make(this.findViewById(android.R.id.content), "Event deleted.", 2000).show();
    }

    public void editGig(GigModel gigModel) {
        intent = new Intent(this, AddEvent.class);
        intent.putExtra("eventId", gigModel.getId());
        intent.putExtra("eventBand", gigModel.getBand());
        intent.putExtra("eventTown", gigModel.getTown());
        intent.putExtra("eventTime", gigModel.getTime());
        intent.putExtra("eventDate", gigModel.getDate());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
