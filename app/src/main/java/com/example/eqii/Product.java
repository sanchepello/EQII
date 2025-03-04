package com.example.eqii;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public double calories;
    public double proteins;
    public double fats;
    public double carbs;

    public Product(String name, double calories, double proteins, double fats, double carbs) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }
}