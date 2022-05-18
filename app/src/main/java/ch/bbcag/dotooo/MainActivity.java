package ch.bbcag.dotooo;

import static androidx.room.util.StringUtil.newStringBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.bbcag.dotooo.adapter.TaskAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Task;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFloatingActionButton();
        addToDosToClickableList();
    }

    public String getCorrectDateStringFromDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);

        int yearInt = Integer.parseInt(formattedDate.substring(0, 4));
        int monthInt = Integer.parseInt(formattedDate.substring(5, 7));
        int dayInt = Integer.parseInt(formattedDate.substring(8, 10));

        String year = Integer.toString(yearInt - 1900);
        String month = Integer.toString(monthInt-1);
        String day = Integer.toString(dayInt);

        return day + "-" + month + "-" + year;
    }

    private void addToDosToClickableList() {
        ListView listView = findViewById(R.id.list);

        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        TaskRoomDao taskDao = database.getTaskDao();

        TaskAdapter arrayAdapter = new TaskAdapter((ArrayList<Task>) taskDao.getAll(), getApplicationContext());
        listView.setAdapter(arrayAdapter);

        // setup click listener
        AdapterView.OnItemClickListener mListClickHandler = (parent, v, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
            Task selected = (Task) parent.getItemAtPosition(position);

            intent.putExtra("taskId", selected.getId());
            intent.putExtra("taskTitle", selected.getTitle());
            intent.putExtra("taskDescription", selected.getDescription());
            intent.putExtra("taskColorHex", selected.getColorHex());
            String formattedDate = getCorrectDateStringFromDate(selected.getDate());
            intent.putExtra("taskDate", formattedDate);
            startActivity(intent);
        };
        listView.setOnItemClickListener(mListClickHandler);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                startActivity(intent);
            }
        });
    }
}