package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.categories.EditCategoryActivity;
import com.example.eventplanner.activities.events.EventTypeEditActivity;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CategoryRepository;

import java.util.ArrayList;

public class CategoryCrudListAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> aCategories;
    private CategoryRepository categoryRepo;

    public CategoryCrudListAdapter(Context context, ArrayList<Category> categories){
        super(context, R.layout.category_card_crud, categories);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_card_crud,
                    parent, false);
        }
        LinearLayout categoryCard = convertView.findViewById(R.id.category_card_item);
        TextView categoryName = convertView.findViewById(R.id.category_name);
        TextView categoryDescription = convertView.findViewById(R.id.category_description);
        TextView categorySubcategories = convertView.findViewById(R.id.category_subcategories);

        ImageButton editButton = convertView.findViewById(R.id.btnEdit);

        editButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + category.getName() +
                    ", id: " + category.getId());
            Intent intent = new Intent(getContext(), EditCategoryActivity.class);
            intent.putExtra("category", category);
            getContext().startActivity(intent);
        });

        if(category != null){
            categoryName.setText(category.getName());
            categoryDescription.setText(category.getDescription());
            //categorySubcategories.setText("Subcategories: " + category.getSubcategories());
            // Prikazivanje naziva podkategorija
            if (category.getSubcategories() != null && !category.getSubcategories().isEmpty()) {
                StringBuilder subcategoryNames = new StringBuilder();
                for (Subcategory subcategory : category.getSubcategories()) {
                    subcategoryNames.append(subcategory.getName()).append(", ");
                }
                // Uklanjanje poslednjeg zareza
                subcategoryNames.deleteCharAt(subcategoryNames.length() - 2);
                categorySubcategories.setText("Subcategories: " + subcategoryNames.toString());
            } else {
                categorySubcategories.setText("No Subcategories");
            }

            categoryCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + category.getName() + ", id: " +
                        category.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + category.getName()  +
                        ", id: " + category.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }


        //ImageButton editButton = convertView.findViewById(R.id.btnEdit);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDelete);


        categoryRepo = new CategoryRepository();
        deleteButton.setOnClickListener(v -> {
            Log.d("Klik", "klik");
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Are you sure you want to delete the category " + category.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (category.getSubcategories() != null && !category.getSubcategories().isEmpty()) {
                                Toast.makeText(getContext(), "Cannot delete category with subcategories", Toast.LENGTH_SHORT).show();
                            } else {
                                category.setDeleted(true);
                                categoryRepo.updateCategory(category);

                                aCategories.remove(category);
                                notifyDataSetChanged();
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = dialog.create();
            alert.show();
        });



        return convertView;
    }
}
