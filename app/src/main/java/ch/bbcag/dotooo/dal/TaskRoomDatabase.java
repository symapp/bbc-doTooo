package ch.bbcag.dotooo.dal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {Task.class},
        version = 1
)
public abstract class TaskRoomDatabase extends RoomDatabase {
    private static TaskRoomDatabase INSTANCE;

    public abstract TaskRoomDao getTaskDao();

    public static synchronized TaskRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TaskRoomDatabase.class, "BadiDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

}
