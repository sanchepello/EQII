package com.example.eqii;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MealProductDao {
    @Insert
    void insert(MealProduct mealProduct);

    @Query("SELECT * FROM meal_products WHERE mealId = :mealId")
    List<MealProduct> getProductsForMeal(int mealId);
}

