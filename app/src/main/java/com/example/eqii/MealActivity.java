package com.example.eqii;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MealActivity extends AppCompatActivity {
    private AppDatabase db;
    private MealDao mealDao;
    private MealProductDao mealProductDao;
    private ProductDao productDao;

    private Spinner mealTypeSpinner;
    private TextView selectedTimeText;
    private String selectedTime;
    private ListView productListView;
    private Meal currentMeal; // Текущий приём пищи

    private final ActivityResultLauncher<Intent> addProductLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Log.d("MealActivity", "Возвращаемся из AddProductsToMealActivity, обновляем список продуктов.");
                    if (currentMeal != null) {
                        loadMealProducts();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        db = AppDatabase.getInstance(this);
        mealDao = db.mealDao();
        mealProductDao = db.mealProductDao();
        productDao = db.productDao();
        productListView = findViewById(R.id.productListView);

        mealTypeSpinner = findViewById(R.id.mealTypeSpinner);
        selectedTimeText = findViewById(R.id.selectedTimeText);
        Button selectTimeButton = findViewById(R.id.selectTimeButton);
        Button addProductButton = findViewById(R.id.addProductButton);
        Button saveMealButton = findViewById(R.id.saveMealButton);

        // Заполняем Spinner готовыми вариантами
        String[] mealTypes = {"Завтрак", "Обед", "Полдник", "Ужин", "Ночной жор"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mealTypes);
        mealTypeSpinner.setAdapter(adapter);

        // Устанавливаем текущее время в формате "ЧЧ:ММ"
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        selectedTime = sdf.format(calendar.getTime());
        selectedTimeText.setText("Выбрано время: " + selectedTime);

        // Обработчик выбора времени
        selectTimeButton.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                selectedTimeText.setText("Выбрано время: " + selectedTime);
            }, hour, minute, true).show();
        });

        // Обработчик нажатия кнопки "Добавить продукт"
        addProductButton.setOnClickListener(v -> {
            if (currentMeal == null) {
                String mealName = mealTypeSpinner.getSelectedItem().toString();
                currentMeal = new Meal(mealName, selectedTime);
                long newMealId = mealDao.insert(currentMeal);
                currentMeal.id = (int) newMealId;

                Log.d("MealActivity", "Создан новый Meal с ID: " + currentMeal.id);
            }

            Log.d("MealActivity", "Открываем AddProductsToMealActivity для mealId: " + currentMeal.id);
            Intent intent = new Intent(MealActivity.this, AddProductsToMealActivity.class);
            intent.putExtra("mealId", currentMeal.id);
            addProductLauncher.launch(intent);
        });

        // Обработчик сохранения приёма пищи
        saveMealButton.setOnClickListener(v -> {
            if (currentMeal == null) {
                Toast.makeText(this, "Сначала добавьте продукты!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохраняем приём пищи в БД
            long mealId = mealDao.insert(currentMeal);
            currentMeal.id = (int) mealId;

            Toast.makeText(this, "Приём пищи сохранён!", Toast.LENGTH_SHORT).show();
            finish(); // Закрываем активити
        });
    }

    private void loadMealProducts() {
        if (currentMeal == null) {
            Log.e("MealActivity", "Ошибка! Невозможно загрузить продукты, так как currentMeal == null!");
            return;
        }

        List<MealProduct> mealProducts = mealProductDao.getProductsForMeal(currentMeal.id);
        List<Product> products = new ArrayList<>();

        for (MealProduct mealProduct : mealProducts) {
            Product product = productDao.getProductById(mealProduct.productId);
            if (product != null) {
                products.add(product);
            }
        }

        Log.d("MealActivity", "Обновляем список продуктов, найдено: " + products.size());

        ProductAdapter adapter = new ProductAdapter(this, products);
        productListView.setAdapter(adapter);
    }
}
