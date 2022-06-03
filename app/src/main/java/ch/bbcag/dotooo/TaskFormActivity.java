package ch.bbcag.dotooo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.bbcag.dotooo.adapter.ColorAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Color;
import ch.bbcag.dotooo.entity.Task;
import ch.bbcag.dotooo.helper.DateFormatter;

public class TaskFormActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Date selectedDate;
    private String selectedColor;
    private Task taskToEdit;

    private AlertDialog.Builder errorDialogBuilder;

    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        // setup actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // setup errorDialogBuilder
        errorDialogBuilder = new AlertDialog.Builder(this);
        errorDialogBuilder.setTitle("Error").setPositiveButton(R.string.ok, null);

        loadTask();
        initInputs();
        initDatePicker();
        initColorSpinner();
        initButtons();

        if (taskToEdit == null) {
            setTitle(R.string.create_activity_name);
        } else {
            setTitle(taskToEdit.getTitle() + " - Edit");
        }
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

    private void loadTask() {
        Intent intent = getIntent();

        if (!intent.hasExtra("taskId")) return;

        int id = intent.getIntExtra("taskId", 0);
        try {
            taskToEdit = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(id);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't load task... Try again later.").create().show();
            onBackPressed();
        }
    }

    private void initInputs() {
        if (taskToEdit == null) return;

        EditText editTextTitle = (EditText) findViewById(R.id.text_input_task_name);
        editTextTitle.setText(taskToEdit.getTitle(), TextView.BufferType.EDITABLE);

        EditText exitTextDescription = (EditText) findViewById(R.id.text_input_task_description);
        exitTextDescription.setText(taskToEdit.getDescription(), TextView.BufferType.EDITABLE);

    }

    private void initDatePicker() {
        dateButton = findViewById(R.id.datePickerButton);

        if (taskToEdit == null) dateButton.setText(getTodaysDate());
        else dateButton.setText(DateFormatter.format(taskToEdit.getDate()));

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

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return DateFormatter.makeDateString(day, month, year);
    }

    private void initColorSpinner() {
        Spinner colorSpinner = findViewById(R.id.color_picker);

        ColorAdapter colorAdapter = new ColorAdapter(getApplicationContext(), false);
        colorSpinner.setAdapter(colorAdapter);

        if (taskToEdit != null){
            Color savedColor = Color.valueOf(taskToEdit.getColorName().toUpperCase(Locale.ROOT));
            for (int i = 0; i < colorAdapter.getCount(); i++) {
                Color color = (Color) colorAdapter.getItem(i);
                if (color != null && color.equals(savedColor)) {
                    colorSpinner.setSelection(i);
                }
            }
        }

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Color[] colors = Color.class.getEnumConstants();
                assert colors != null;
                selectedColor = colors[i].getDisplayName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
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

    private void saveTask() {
        EditText editTextTitle = findViewById(R.id.text_input_task_name);
        String title = editTextTitle.getText().toString();
        if (title.startsWith("?")) title = title.replace("?", "");
        if (TextUtils.isEmpty(title)) {
            title = "Unnamed Task";
        }

        EditText editTextDescription = findViewById(R.id.text_input_task_description);
        String description = editTextDescription.getText().toString();


        if (selectedDate == null) selectedDate = new Date();

        if (taskToEdit != null) {
            taskToEdit.setTitle(title);
            taskToEdit.setDescription(description);
            taskToEdit.setDate(selectedDate);
            taskToEdit.updateColorByName(selectedColor);
        }

        try {
            TaskRoomDao dao = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao();
            if (taskToEdit == null)
                dao.insert(new Task(title, description, selectedDate, selectedColor));
            else
                dao.update(taskToEdit);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't save task... Try again later.").create().show();
            return;
        }

        onBackPressed();
    }
}