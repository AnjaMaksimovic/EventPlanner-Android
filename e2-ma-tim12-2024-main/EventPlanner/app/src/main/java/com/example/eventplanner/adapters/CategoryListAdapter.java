package com.example.eventplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> aCategories;

    public CategoryListAdapter(Context context, ArrayList<Category> categories){
        super(context, R.layout.category_card, categories);
        aCategories = categories;

    }

    @Override
    public int getCount() {
        return aCategories.size();
    }


    @Nullable
    @Override
    public Category getItem(int position) {
        return aCategories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_card,
                    parent, false);
        }
        LinearLayout categoryCard = convertView.findViewById(R.id.category_card_item);
        TextView categoryName = convertView.findViewById(R.id.category_name);
        TextView categoryDescription = convertView.findViewById(R.id.category_description);
        TextView categorySubcategories = convertView.findViewById(R.id.category_subcategories);

        if(category != null){
            categoryName.setText(category.getName());
            categoryDescription.setText(category.getDescription());
            categorySubcategories.setText("Categories: " + category.getSubcategories());
            categoryCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + category.getName() + ", id: " +
                        category.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + category.getName()  +
                        ", id: " + category.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
