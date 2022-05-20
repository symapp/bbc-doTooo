package ch.bbcag.dotooo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ch.bbcag.dotooo.adapter.ColorAdapter;
import ch.bbcag.dotooo.entity.Color;

public class Filter extends Fragment {

    private Context context;
    private FilterViewModel viewModel;
    private Date selectedDate = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert container != null;
        this.context = container.getContext();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initColorPicker();
        initDatePicker();
        initCompletedSpinner();
    }

    private void initCompletedSpinner() {
        // setup
        Spinner completedSpinner = (Spinner) requireView().findViewById(R.id.completed_button);

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Uncompleted");
        choices.add("Completed");
        choices.add("All");

        ArrayAdapter<String> completedSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, choices);
        completedSpinner.setAdapter(completedSpinnerAdapter);


        // set based on viewModel
        if (viewModel.getSelectedOnlyUncompleted().getValue() == null) {
            completedSpinner.setSelection(2);
        } else if (viewModel.getSelectedOnlyUncompleted().getValue()) {
            completedSpinner.setSelection(0);
        } else {
            completedSpinner.setSelection(1);
        }

        // listeners
        completedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Boolean value = null;
                if (adapterView.getSelectedItem().equals("Completed")) value = Boolean.FALSE;
                else if (adapterView.getSelectedItem().equals("Uncompleted")) value = Boolean.TRUE;
                viewModel.setSelectedOnlyUncompleted(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        completedSpinner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                viewModel.setSelectedOnlyUncompleted(null);
                completedSpinner.setSelection(2);
                return true;
            }
        });
    }

    private void initColorPicker() {
        // setup
        Spinner colorSpinner = requireView().findViewById(R.id.color_picker);

        ColorAdapter colorAdapter = new ColorAdapter(context, true);
        colorSpinner.setAdapter(colorAdapter);

        // set based on viewModel
        Color selectedColor = viewModel.getSelectedColor().getValue();
        for (int i = 0; i < colorAdapter.getCount(); i++) {
            Color color = (Color) colorAdapter.getItem(i);
            if (selectedColor == null) {
                if (color == null) {
                    colorSpinner.setSelection(i);
                }
            } else {
                if (color != null && color.equals(selectedColor)) {
                    colorSpinner.setSelection(i);
                }
            }
        }

        // listeners
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.setSelectedColor((Color) adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        colorSpinner.setOnLongClickListener(view -> {
            viewModel.setSelectedColor(null);
            colorSpinner.setSelection(0);
            return true;
        });
    }

    @SuppressLint("SetTextI18n")
    private void initDatePicker() {
        Button dateButton = requireView().findViewById(R.id.datePickerButton);

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            Date selectedDate = new Date(year-1900, month, day);
            month = month + 1;
            String date = makeDateString(day, month, year);
            dateButton.setText(date);
            viewModel.setSelectedDate(selectedDate);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);

        dateButton.setOnClickListener(view -> datePickerDialog.show());

        dateButton.setOnLongClickListener(view -> {
            if (dateButton.getText() == "All") {
                selectedDate = new Date();
                dateButton.setText(getTodaysDate());
                viewModel.setSelectedDate(selectedDate);
            } else {
                selectedDate = null;
                dateButton.setText("All");
                viewModel.setSelectedDate(null);
            }
            return true;
        });

        // set based on viewModel
        Date date = viewModel.getSelectedDate().getValue();
        if (date == null) {
            dateButton.setText("All");
            selectedDate = null;
        } else {
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(date);
            selectedDate = date;
            dateButton.setText(makeDateString(dateCal.get(Calendar.DAY_OF_MONTH), dateCal.get(Calendar.MONTH), dateCal.get(Calendar.YEAR)));
        }
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
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
}