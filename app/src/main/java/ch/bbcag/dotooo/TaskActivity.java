package ch.bbcag.dotooo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Task;
import ch.bbcag.dotooo.helper.DateFormatter;

public class TaskActivity extends AppCompatActivity {

    private Task task;

    private int id;

    private String title;

    private String description;

    private Date date;

    private String colorHex;

    private AlertDialog.Builder errorDialogBuilder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // setup errorDialogBuilder
        errorDialogBuilder = new AlertDialog.Builder(this);
        errorDialogBuilder.setTitle("Error").setPositiveButton(R.string.ok, null);

        loadTask();

        setTitle(title);

        TextView titleTextField = findViewById(R.id.title);
        titleTextField.setText(title);
        TextView descriptionTextField = findViewById(R.id.description);
        descriptionTextField.setText(description);
        if (task.getDescription().length() <= 0) descriptionTextField.setTextColor(Color.parseColor("#99000000"));

        TextView dateTextField = findViewById(R.id.date);
        dateTextField.setText(getDateAsString(date));

        CardView colorCardView = findViewById(R.id.detailColorView);
        colorCardView.setCardBackgroundColor(Color.parseColor(colorHex));

        Button button = findViewById(R.id.completeButton);
        if (task.getDone()) button.setText("Mark uncompleted");
    }

    private void loadTask() {
        Intent intent = getIntent();
        id = intent.getIntExtra("taskId", 0);

        try {
            task = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(id);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't load task... Try again later.").create().show();
            onBackPressed();
            return;
        }

        title = task.getTitle();
        description = task.getDescription().length() > 0 ? task.getDescription() : "No Description";
        date = task.getDate();
        colorHex = task.getColorHex();
    }

    private String getDateAsString(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        int year = Integer.parseInt(formattedDate.substring(0, 4));
        int month = Integer.parseInt(formattedDate.substring(5, 7));
        int day = Integer.parseInt(formattedDate.substring(8, 10));

        year = year - 1900;

        if (year < 1000) {
            year = year + 1900;
        }

        return DateFormatter.makeDateString(day, month, year);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (itemId == R.id.action_delete) {
            deleteTask();
            onBackPressed();
            return true;
        }
        if (itemId == R.id.action_edit) {
            redirectToEdit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTask() {
        try {
            TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().deleteById(id);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't delete task... Try again later.").create().show();
        }
    }

    private void redirectToEdit() {
        try {
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            Task selected = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(id);
            intent.putExtra("taskId", selected.getId());
            startActivity(intent);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't load task... Try again later.").create().show();
        }
    }

    public void toggleCompleted(View view) {
        try {
            task.setDone(!task.getDone());
            TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().update(task);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't update task... Try again later.").create().show();
            return;
        }

        onBackPressed();
    }
}