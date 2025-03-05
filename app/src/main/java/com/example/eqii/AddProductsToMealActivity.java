package com.example.eqii;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AddProductsToMealActivity extends AppCompatActivity {
    private AppDatabase db;
    private ProductDao productDao;
    private MealProductDao mealProductDao;
    private ListView productListView;
    private View addProductLayout;
    private Button addNewProductButton, saveProductButton, addSelectedProductsButton;
    private EditText productNameInput, caloriesInput, proteinInput, fatInput, carbsInput;
    private ProductSelectAdapter adapter;
    private int mealId; // ID текущего приема пищи
    private List<Product> selectedProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_to_meal);

        db = AppDatabase.getInstance(this);
        productDao = db.productDao();
        mealProductDao = db.mealProductDao();

        productListView = findViewById(R.id.productListView);
        addProductLayout = findViewById(R.id.addProductLayout);
        addNewProductButton = findViewById(R.id.addNewProductButton);
        saveProductButton = findViewById(R.id.saveProductButton);
        addSelectedProductsButton = findViewById(R.id.addSelectedProductsButton);

        productNameInput = findViewById(R.id.productNameInput);
        caloriesInput = findViewById(R.id.caloriesInput);
        proteinInput = findViewById(R.id.proteinInput);
        fatInput = findViewById(R.id.fatInput);
        carbsInput = findViewById(R.id.carbsInput);

        mealId = getIntent().getIntExtra("mealId", -1);
        if (mealId == -1) {
            Toast.makeText(this, "Ошибка: mealId не передан!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProducts();

        // Нажатие на "Добавить новый продукт"
        addNewProductButton.setOnClickListener(v -> {
            Log.d("AddProductsToMealActivity", "Открываем экран добавления нового продукта!");
            productListView.setVisibility(View.GONE);
            addNewProductButton.setVisibility(View.GONE);
            addSelectedProductsButton.setVisibility(View.GONE);
            addProductLayout.setVisibility(View.VISIBLE);
        });

        // Нажатие на "Сохранить продукт"
        saveProductButton.setOnClickListener(v -> {
            saveNewProduct();
        });

        // Нажатие на "Добавить выбранные продукты"
        addSelectedProductsButton.setOnClickListener(v -> {
            addSelectedProductsToMeal();
        });
    }

    private void saveNewProduct() {
        String name = productNameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Введите название продукта!", Toast.LENGTH_SHORT).show();
            return;
        }

        int calories = parseIntOrZero(caloriesInput.getText().toString());
        float protein = parseFloatOrZero(proteinInput.getText().toString());
        float fat = parseFloatOrZero(fatInput.getText().toString());
        float carbs = parseFloatOrZero(carbsInput.getText().toString());

        Log.d("AddProductsToMealActivity", "Создаём продукт: " + name + ", Калории: " + calories);

        Product newProduct = new Product(name, calories, protein, fat, carbs);
        productDao.insert(newProduct);

        Log.d("AddProductsToMealActivity", "Продукт сохранён!");

        Toast.makeText(this, "Продукт добавлен!", Toast.LENGTH_SHORT).show();

        // Очищаем поля
        productNameInput.setText("");
        caloriesInput.setText("");
        proteinInput.setText("");
        fatInput.setText("");
        carbsInput.setText("");

        // Показываем обратно список продуктов
        addProductLayout.setVisibility(View.GONE);
        productListView.setVisibility(View.VISIBLE);
        addNewProductButton.setVisibility(View.VISIBLE);
        addSelectedProductsButton.setVisibility(View.VISIBLE);

        // Обновляем список
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = productDao.getAllProducts();
        adapter = new ProductSelectAdapter(this, products);
        productListView.setAdapter(adapter);
    }

    private void addSelectedProductsToMeal() {
        selectedProducts = adapter.getSelectedProducts();
        if (selectedProducts.isEmpty()) {
            Toast.makeText(this, "Выберите хотя бы один продукт!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Product product : selectedProducts) {
            MealProduct mp = new MealProduct(mealId, product.id, 100);
            mealProductDao.insert(mp);
            Log.d("AddProductsToMealActivity", "Добавлен продукт: " + product.name + " в mealId: " + mealId);
        }

        Toast.makeText(this, "Продукты добавлены в приём пищи!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private float parseFloatOrZero(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
}
