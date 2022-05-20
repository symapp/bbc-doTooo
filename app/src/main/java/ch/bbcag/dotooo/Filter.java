package ch.bbcag.dotooo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
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

    private ItemViewModel viewModel;

    private Date selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        initColorPicker();
        initDatePicker();
        initCompletedSpinner();
    }

    private void initCompletedSpinner() {
        Spinner completedSpinner = (Spinner) getView().findViewById(R.id.completed_button);

        ArrayList<String> choices = new ArrayList<>();
        choices.add("All");
        choices.add("Completed");
        choices.add("Uncompleted");

        ArrayAdapter<String> completedSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, choices);
        completedSpinner.setAdapter(completedSpinnerAdapter);

        completedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Boolean value = null;
                if (adapterView.getSelectedItem().equals("Completed")) value = Boolean.TRUE;
                else if (adapterView.getSelectedItem().equals("Uncompleted")) value = Boolean.FALSE;
                ((MainActivity) requireActivity()).setFilter_only_completed(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initColorPicker() {
        Spinner colorSpinner = getView().findViewById(R.id.color_picker);

        ColorAdapter colorAdapter = new ColorAdapter(context, true);
        colorSpinner.setAdapter(colorAdapter);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity) requireActivity()).setFilter_color((Color) adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initDatePicker() {
        Button dateButton = getView().findViewById(R.id.datePickerButton);

        Button finalDateButton = dateButton;
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                selectedDate = new Date(year-1900, month, day);
                month = month + 1;
                String date = makeDateString(day, month, year);
                finalDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        dateButton = getView().findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
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