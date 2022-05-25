package ch.bbcag.dotooo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Task;

public class TaskActivity extends AppCompatActivity {

    private Task task;

    private int id;

    private String title;

    private String description;

    private Date date;

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

        task = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(id);

        title = task.getTitle();
        description = task.getDescription().length() > 0 ? task.getDescription() : "No Description";
        date = task.getDate();
        colorHex = task.getColorHex();

        setTitle(title);

        TextView titleTextField = (TextView) findViewById(R.id.title);
        titleTextField.setText(title);
        TextView descriptionTextField = (TextView) findViewById(R.id.description);
        descriptionTextField.setText(description);

        TextView dateTextField = (TextView) findViewById(R.id.date);
        dateTextField.setText(getDateAsString(date));

        CardView colorCardView = (CardView) findViewById(R.id.detailColorView);
        colorCardView.setCardBackgroundColor(Color.parseColor(colorHex));
    }

    private String getDateAsString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        int year = Integer.parseInt(formattedDate.substring(0, 4));
        int month = Integer.parseInt(formattedDate.substring(5, 7));
        int day = Integer.parseInt(formattedDate.substring(8, 10));

        year = year - 1900;

        if (year < 1000) {
            year = year + 1900;
        }

        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "th " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "January";
        if (month == 2)
            return "February";
        if (month == 3)
            return "March";
        if (month == 4)
            return "April";
        if (month == 5)
            return "May";
        if (month == 6)
            return "June";
        if (month == 7)
            return "Juli";
        if (month == 8)
            return "August";
        if (month == 9)
            return "September";
        if (month == 10)
            return "October";
        if (month == 11)
            return "November";
        if (month == 12)
            return "December";

        //default should never happen
        return "January";
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
            delteTask();
            redirectToHome();
            return true;
        }
        if (itemId == R.id.action_edit) {
            redirectToEdit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void delteTask() {
        TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().deleteById(id);
    }

    public void redirectToHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void redirectToEdit() {
        Intent intent = new Intent(getApplicationContext(), EditActivity.class);
        Task selected = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(id);
        intent.putExtra("taskId", selected.getId());
        startActivity(intent);
    }


    public void updateTaskById(Integer id) {
        task.setDone(true);
        TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().update(task);
    }


    public void completeTask(View view) {
        updateTaskById(id);
        redirectToHome();
    }
}