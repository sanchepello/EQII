package com.example.eqii;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private MealDao mealDao;
    private ListView mealsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mealsListView = findViewById(R.id.mealsListView);
        TextView currentDateText = findViewById(R.id.currentDateText);

        Button addMealButton = findViewById(R.id.addMealButton);

        if (addMealButton == null) {
            Log.e("MainActivity", "Ошибка! Кнопка addMealButton не найдена в XML!");
            return;
        }

        addMealButton.setOnClickListener(v -> {
            Log.d("MainActivity", "Нажата кнопка 'Добавить приём пищи'!");

            Intent intent = new Intent(MainActivity.this, MealActivity.class);
            startActivity(intent);
        });

        // Получаем текущую дату
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        String currentDate = "Сегодня: " + sdf.format(calendar.getTime());

        // Устанавливаем её в TextView
        currentDateText.setText(currentDate);

        Log.d("MainActivity", "Обновлена дата на главном экране: " + currentDate);

        db = AppDatabase.getInstance(this);
        mealDao = db.mealDao();

        mealsListView.setOnItemClickListener((parent, view, position, id) -> {
            List<Meal> meals = mealDao.getAllMeals();
            if (position >= meals.size()) {
                Log.e("MainActivity", "Ошибка: кликнули по несуществующему приёму пищи.");
                return;
            }

            Meal selectedMeal = meals.get(position);

            Log.d("MainActivity", "Нажали на приём пищи: " + selectedMeal.name + ", ID: " + selectedMeal.id);

            Intent intent = new Intent(MainActivity.this, MealDetailActivity.class);
            intent.putExtra("mealId", selectedMeal.id);
            startActivity(intent);
        });
        loadMeals();
    }

    private void loadMeals() {
        List<Meal> meals = mealDao.getAllMeals();

        if (meals.isEmpty()) {
            Log.d("MainActivity", "Нет приёмов пищи в базе!");
            Toast.makeText(this, "Приёмов пищи пока нет!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> mealDisplayList = new ArrayList<>();

        for (Meal meal : meals) {
            try {
                String formattedTime;

                if (meal.time.length() == 5) { // Если время записано как "ЧЧ:ММ"
                    formattedTime = meal.time;
                } else { // Если время в формате "yyyy-MM-dd HH:mm"
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    Date date = inputFormat.parse(meal.time);
                    formattedTime = outputFormat.format(date);
                }

                String displayText = meal.name + " - " + formattedTime;
                mealDisplayList.add(displayText);
                Log.d("MainActivity", "Добавляем в список: " + displayText);
            } catch (Exception e) {
                Log.e("MainActivity", "Ошибка форматирования времени: " + meal.time, e);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mealDisplayList);
        mealsListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMeals(); // Обновляем список при возвращении на главный экран
    }
}
