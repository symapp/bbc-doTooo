package ch.bbcag.dotooo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list);
        ArrayAdapter<Task> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);

        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        TaskRoomDao taskDao= database.getTaskDao();

        Task task1 = new Task();
        task1.setTitle("First Task");
        task1.setDescription("asdfasdfasdf");
        taskDao.insert(task1);

        Task task2 = new Task();
        task2.setTitle("Second Task");
        task2.setDescription("Shessh what a task");
        taskDao.insert(task2);




        arrayAdapter.addAll(taskDao.getAll());
        listView.setAdapter(arrayAdapter);
    }
}