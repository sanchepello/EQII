package com.example.eqii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

public class ProductSelectAdapter extends ArrayAdapter<Product> {
    private final List<Product> productList;
    private final List<Product> selectedProducts = new ArrayList<>();

    public ProductSelectAdapter(Context context, List<Product> productList) {
        super(context, R.layout.list_item_product_select, productList);
        this.productList = productList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_product_select, parent, false);
        }

        Product product = productList.get(position);

        TextView productName = convertView.findViewById(R.id.productName);
        TextView productInfo = convertView.findViewById(R.id.productInfo);
        CheckBox checkBox = convertView.findViewById(R.id.productCheckBox);

        productName.setText(product.name);
        productInfo.setText("Кал: " + product.calories + " | Б: " + product.proteins +
                " | Ж: " + product.fats + " | У: " + product.carbs);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedProducts.add(product);
            } else {
                selectedProducts.remove(product);
            }
        });

        return convertView;
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }
}
