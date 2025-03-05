package com.example.eqii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
        }

        Product product = productList.get(position);

        TextView productName = convertView.findViewById(R.id.productName);
        TextView productInfo = convertView.findViewById(R.id.productInfo);

        productName.setText(product.name);
        productInfo.setText("Кал: " + product.calories + " | Б: " + product.proteins +
                " | Ж: " + product.fats + " | У: " + product.carbs);
        return convertView;
    }
}
