package ch.bbcag.dotooo.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Color {
    @PrimaryKey
    @ColumnInfo(name="colorId")
    private int id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="hex")
    private String hex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }
}
