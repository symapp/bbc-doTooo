package ch.bbcag.dotooo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    private int id;

    private String title;

    private String description;

    private String date;

    private String colorHex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        initFloatingActionButton();
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_task);
        Intent intent = getIntent();
        id = intent.getIntExtra("taskId", 0);
        title = intent.getStringExtra("taskTitle");
        description = intent.getStringExtra("taskDescription");
        date = intent.getStringExtra("taskDate");
        colorHex = intent.getStringExtra("taskColorHex");
        setTitle(title);
        TextView titleTextField = (TextView) findViewById(R.id.title);
        titleTextField.setText(title);
        TextView descriptionTextField = (TextView) findViewById(R.id.description);
        descriptionTextField.setText(description);
        TextView dateTextField = (TextView) findViewById(R.id.date);
        dateTextField.setText(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void initFloatingActionButton() {
//        FloatingActionButton fab = findViewById(R.id.fab2);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}