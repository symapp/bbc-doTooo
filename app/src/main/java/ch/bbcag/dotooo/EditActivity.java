package ch.bbcag.dotooo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.bbcag.dotooo.adapter.ColorAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Color;
import ch.bbcag.dotooo.entity.Task;
import ch.bbcag.dotooo.helper.DateFormatter;

public class EditActivity extends AppCompatActivity {

    private Task task;

    private DatePickerDialog datePickerDialog;

    private Button dateButton;

    private AlertDialog.Builder errorDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // setup errorDialogBuilder
        errorDialogBuilder = new AlertDialog.Builder(this);
        errorDialogBuilder.setTitle("Error").setPositiveButton(R.string.ok, null);

        loadTask();
        initButtons();
        initDatePicker();
        setValues();
    }

    private void loadTask() {
        Intent intent = getIntent();

        int id = intent.getIntExtra("taskId", 0);

        try {
            task = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(id);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't load task... Try again later.").create().show();
            onBackPressed();
        }
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
        colorSpinner.setAdapter(colorAdapter);

        Color selectedColor = Color.valueOf(task.getColorName().toUpperCase(Locale.ROOT));
        for (int i = 0; i < colorAdapter.getCount(); i++) {
            Color color = (Color) colorAdapter.getItem(i);
            if (color != null && color.equals(selectedColor)) {
                colorSpinner.setSelection(i);
            }
        }

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Color[] colors = Color.class.getEnumConstants();
                assert colors != null;
                task.setColorName(colors[i].getDisplayName());
                task.setColorHex(colors[i].getHex());
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
                saveTask();
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initDatePicker() {
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getDateAsString(task.getDate()));

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = DateFormatter.makeDateString(day, month, year);
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

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void saveTask() {
        EditText editTextTitle = (EditText) findViewById(R.id.text_input_task_name);
        task.setTitle(editTextTitle.getText().toString());
        if (TextUtils.isEmpty(task.getTitle())) {
            task.setTitle("Unnamed Task");
        }

        EditText editTextDescription = (EditText) findViewById(R.id.text_input_task_description);
        task.setDescription(editTextDescription.getText().toString());

        try {
            TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().update(task);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't update task... Try again later.").create().show();
            return;
        }

        this.onBackPressed();
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