package com.example.eqii;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MealDetailActivity extends AppCompatActivity {
    private AppDatabase db;
    private MealDao mealDao;
    private MealProductDao mealProductDao;
    private ProductDao productDao;
    private TextView mealNameText, mealSummaryText;
    private ListView productListView;
    private int mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        db = AppDatabase.getInstance(this);
        mealDao = db.mealDao();
        mealProductDao = db.mealProductDao();
        productDao = db.productDao();

        mealNameText = findViewById(R.id.mealNameText);
        mealSummaryText = findViewById(R.id.mealSummaryText);
        productListView = findViewById(R.id.productListView);

        mealId = getIntent().getIntExtra("mealId", -1);
        if (mealId == -1) {
            finish();
        }

        loadMealDetails();
    }

    private void loadMealDetails() {
        // Загружаем данные о приёме пищи
        Meal meal = mealDao.getMealById(mealId);
        mealNameText.setText(meal.name + " (" + meal.time + ")");

        // Загружаем все продукты, относящиеся к этому приёму пищи
        List<MealProduct> mealProducts = mealProductDao.getProductsForMeal(mealId);
        List<Product> products = new ArrayList<>();
        int totalCalories = 0;
        float totalProtein = 0, totalFat = 0, totalCarbs = 0;

        for (MealProduct mealProduct : mealProducts) {
            Product product = productDao.getProductById(mealProduct.productId);
            if (product != null) {
                products.add(product);
                totalCalories += product.calories;
                totalProtein += product.proteins;
                totalFat += product.fats;
                totalCarbs += product.carbs;
            }
        }

        // Обновляем список продуктов на экране
        ProductAdapter adapter = new ProductAdapter(this, products);
        productListView.setAdapter(adapter);

        // Обновляем суммарные калории и БЖУ
        mealSummaryText.setText("Всего: " + totalCalories + " ккал | Б: " + totalProtein + " г | Ж: " + totalFat + " г | У: " + totalCarbs + " г");
    }
}
