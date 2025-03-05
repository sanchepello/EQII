package com.example.eqii;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Meal.class, MealProduct.class, Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MealDao mealDao();
    public abstract MealProductDao mealProductDao();
    public abstract ProductDao productDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "food_tracker_db")
                            .allowMainThreadQueries() // В реальном проекте лучше убирать
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
