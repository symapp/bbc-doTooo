package ch.bbcag.dotooo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import ch.bbcag.dotooo.dal.TaskRoomDatabase;

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
        CardView colorCardView = (CardView) findViewById(R.id.detailColorView);
        colorCardView.setCardBackgroundColor(Color.parseColor(colorHex));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent = null;
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (itemId == R.id.action_delete) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            delteTask();
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.action_edit) {
            intent = new Intent(getApplicationContext(), EditActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

<<<<<<< HEAD
    private void delteTask() {
        TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().deleteById(id);

=======
    private void redirectToHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void completeTask() {

        redirectToHome();
>>>>>>> 65584282a1f42aa9767eb70b573e04fd32a83de2
    }
}