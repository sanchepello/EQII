package com.example.eqii;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Main extends AppCompatActivity {

    private TextView currentDateText;
    private Button addMealButton;
    private RecyclerView mealList;
    private MealAdapter mealAdapter;
    private ArrayList<String> meals; // Временный список для отображения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDateText = findViewById(R.id.currentDate);
        addMealButton = findViewById(R.id.addMealButton);
        mealList = findViewById(R.id.mealList);

        // Устанавливаем текущую дату
        String currentDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        currentDateText.setText("Сегодня: " + currentDate);

        // Настраиваем RecyclerView
        meals = new ArrayList<>();
        mealAdapter = new MealAdapter(meals);
        mealList.setLayoutManager(new LinearLayoutManager(this));
        mealList.setAdapter(mealAdapter);

        // Кнопка "Добавить прием пищи"
        addMealButton.setOnClickListener(v -> {
            meals.add("Новый приём пищи");
            mealAdapter.notifyDataSetChanged();
        });
    }
}
