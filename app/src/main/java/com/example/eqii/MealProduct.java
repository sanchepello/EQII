package com.example.eqii;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_products",
        foreignKeys = {
                @ForeignKey(entity = Meal.class, parentColumns = "id", childColumns = "mealId"),
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId")
        })
public class MealProduct {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int mealId; // Какому приёму пищи принадлежит продукт
    public int productId; // Какой продукт добавлен в приём пищи
    public int quantity; // Количество (например, 100 г)

    public MealProduct(int mealId, int productId, int quantity) {
        this.mealId = mealId;
        this.productId = productId;
        this.quantity = quantity;
    }
}