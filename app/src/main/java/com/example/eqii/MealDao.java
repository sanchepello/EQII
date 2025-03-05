package com.example.eqii;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MealDao {
    @Insert
    long insert(Meal meal);

    @Query("SELECT * FROM meals")
    List<Meal> getAllMeals();

    @Query("SELECT * FROM meals WHERE id = :mealId")
    Meal getMealById(int mealId);
}
