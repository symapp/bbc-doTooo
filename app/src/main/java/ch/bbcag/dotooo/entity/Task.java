package ch.bbcag.dotooo.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;

import ch.bbcag.dotooo.DateConverter;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter.class)
    private Date date;

    @ColumnInfo(name = "isDone")
    private Boolean isDone;

    @ColumnInfo(name = "colorHex")
    private String colorHex;

    @ColumnInfo(name = "colorName")
    private String colorName;

    public Task(String title, String description, Date selectedDate, String colorName) {
        this.title = title;
        this.description = description;
        this.date = selectedDate;
        isDone = false;
        this.colorName = colorName;
        for (Color color: Color.values()) {
            if (color.getDisplayName().equals(colorName)) {
                setColorHex(color.getHex());
            }
        }
    }

    public Task() {
    }

    public String toString() {
        return title + " " + description + " " + date + " " + colorHex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
