package ch.bbcag.dotooo.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TaskWithColor {
    @Embedded private Color color;
    @Relation(
            parentColumn = "colorId",
            entityColumn = "colorId"
    )
    private Task task;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
