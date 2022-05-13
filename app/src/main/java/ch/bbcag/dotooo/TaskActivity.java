package ch.bbcag.dotooo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class TaskActivity extends AppCompatActivity {

    private int id;

    private String title;

    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_task);
        Intent intent = getIntent();
        id = intent.getIntExtra("taskId", 0);
        title = intent.getStringExtra("taskTitle");
        description = intent.getStringExtra("taskDescription");
        setTitle(title);
        TextView titleTextField = (TextView) findViewById(R.id.title);
        titleTextField.setText(title);
        TextView descriptionTextField = (TextView) findViewById(R.id.description);
        descriptionTextField.setText(description);
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
}