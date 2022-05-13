package ch.bbcag.dotooo.dal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import ch.bbcag.dotooo.Entity.Task;

@Dao
public interface TaskRoomDao {
    @Query("SELECT * FROM task ORDER BY date ASC")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE title LIKE '%:title%' ORDER BY date ASC")
    List<Task> getTasksByTitle(String title);

    @Query("SELECT * FROM task WHERE description LIKE '%:description%' ORDER BY date ASC")
    List<Task> getTasksByDescription(String description);

    @Query("SELECT * FROM task WHERE date = :date ORDER BY date ASC")
    List<Task> getTaskByDate(Date date);

    @Query("SELECT * FROM task WHERE isDone = :done ORDER BY date ASC")
    List<Task> getTasksByCompleted(boolean done);

    @Update
    void update(Task task);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);
}
