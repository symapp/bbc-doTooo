package ch.bbcag.dotooo.dal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import ch.bbcag.dotooo.entity.Task;
import ch.bbcag.dotooo.entity.TaskWithColor;

@Dao
public interface TaskRoomDao {
    @Transaction
    @Query("SELECT * FROM task ORDER BY date ASC")
    List<TaskWithColor> getAll();

    @Transaction
    @Query("SELECT * FROM task WHERE title LIKE '%' || :title || '%' ORDER BY date ASC")
    List<TaskWithColor> getTasksByTitle(String title);

    @Transaction
    @Query("SELECT * FROM task WHERE description LIKE '%' || :description || '%' ORDER BY date ASC")
    List<TaskWithColor> getTasksByDescription(String description);

    @Transaction
    @Query("SELECT * FROM task WHERE date = :date ORDER BY date ASC")
    List<TaskWithColor> getTaskByDate(Date date);

    @Transaction
    @Query("SELECT * FROM task WHERE isDone = :done ORDER BY date ASC")
    List<TaskWithColor> getTasksByCompleted(boolean done);

    @Update
    void update(Task task);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);
}
