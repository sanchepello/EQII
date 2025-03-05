package com.example.eqii;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals")
public class Meal {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String name; // Название (Завтрак, Обед, Ужин)
    public String time; // Полная дата и время (2025-03-06 08:30)

    public Meal(String name, String time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public String toString() {
        return name + " - " + time;
    }
}
