package ch.bbcag.dotooo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import ch.bbcag.dotooo.adapter.ColorAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Color;
import ch.bbcag.dotooo.entity.Task;
import ch.bbcag.dotooo.helper.DateFormatter;

public class CreateTaskActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Date selectedDate = new Date();
    private String selectedColor;

    private AlertDialog.Builder errorDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        setTitle(getString(R.string.create_activity_name));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        // setup errorDialogBuilder
        errorDialogBuilder = new AlertDialog.Builder(this);
        errorDialogBuilder.setTitle("Error").setPositiveButton(R.string.ok, null);

        initColorSpinner();
        initButtons();
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

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return DateFormatter.makeDateString(day, month, year);
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
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            selectedDate = new Date(year - 1900, month, day);
            month = month + 1;
            String date = DateFormatter.makeDateString(day, month, year);
            dateButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    private void initColorSpinner() {
        Spinner colorSpinner = findViewById(R.id.color_picker);

        ColorAdapter colorAdapter = new ColorAdapter(getApplicationContext(), false);
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

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void saveTask() {
        EditText editTextTitle = findViewById(R.id.text_input_task_name);
        String title = editTextTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            title = "Unnamed Task";
        }

        EditText editTextDescription = findViewById(R.id.text_input_task_description);
        String description = editTextDescription.getText().toString();

        try {
            TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().insert(new Task(title, description, selectedDate, selectedColor));
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't save task... Try again later.").create().show();
            return;
        }

        onBackPressed();
    }
}