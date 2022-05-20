package ch.bbcag.dotooo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ch.bbcag.dotooo.adapter.ColorAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Color;
import ch.bbcag.dotooo.entity.Task;

public class EditActivity extends AppCompatActivity {
    private int id;

    private Task task;

    private String selectedColor;

    private DatePickerDialog datePickerDialog;

    private Button dateButton;

    private Date selectedDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit);
        id = intent.getIntExtra("taskId", 0);
        task = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(id);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initButtons();

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getCorrectDateStringFromDate(task.getDate()));

        setValues();
    }



    private String getCorrectDateStringFromDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        int yearInt = Integer.parseInt(formattedDate.substring(0, 4));
        int monthInt = Integer.parseInt(formattedDate.substring(5, 7));
        int dayInt = Integer.parseInt(formattedDate.substring(8, 10));

        String year = Integer.toString(yearInt - 1900);
        String month = Integer.toString(monthInt - 1);
        String day = Integer.toString(dayInt);

        return day + "-" + month + "-" + year;
    }

    private void setValues() {
        EditText editTextTitle = (EditText) findViewById(R.id.text_input_task_name);
        editTextTitle.setText(task.getTitle(), TextView.BufferType.EDITABLE);

        EditText exitTextDescription = (EditText) findViewById(R.id.text_input_task_description);
        exitTextDescription.setText(task.getDescription(), TextView.BufferType.EDITABLE);

        initColorSpinner();
    }

    private void initColorSpinner() {
        Spinner colorSpinner = findViewById(R.id.color_picker);


        ColorAdapter colorAdapter = new ColorAdapter(getApplicationContext(), false);
        colorSpinner.setSelection(5);
        colorSpinner.setAdapter(colorAdapter);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Color[] colors = Color.class.getEnumConstants();
                assert colors != null;
                selectedColor = colors[i].getDisplayName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void initButtons() {
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask(view);
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToHome();
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                task.setDate(new Date(year, month, day));
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
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

        return "January";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }



    private void redirectToHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    private void saveTask(View view) {
        EditText editTextTitle = (EditText) findViewById(R.id.text_input_task_name);
        task.setTitle(editTextTitle.getText().toString());
        if (TextUtils.isEmpty(task.getTitle())) {
            task.setTitle("Unnamed Task");
        }

        EditText editTextDescription = (EditText) findViewById(R.id.text_input_task_description);
        task.setDescription(editTextDescription.getText().toString());

        TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().update(task);

        redirectToHome();
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