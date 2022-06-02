package ch.bbcag.dotooo.dal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import ch.bbcag.dotooo.helper.DateConverter;
import ch.bbcag.dotooo.entity.Task;

@Dao
public interface TaskRoomDao {
    @Transaction
    @Query("SELECT * FROM task ORDER BY date ASC")
    List<Task> getAll();

    @Transaction
    @Query("SELECT * FROM task WHERE taskId = :id")
    Task getById(Integer id);

    @Update
    void update(Task task);

    @Insert
    void insert(Task task);

    @Transaction
    @Query("DELETE FROM Task WHERE taskId = :id")
    void deleteById(Integer id);
}
