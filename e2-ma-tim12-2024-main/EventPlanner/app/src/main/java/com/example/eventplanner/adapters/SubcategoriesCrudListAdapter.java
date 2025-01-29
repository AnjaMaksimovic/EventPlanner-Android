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
import com.example.eventplanner.activities.categories.EditSubcategoryActivity;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;

import java.util.ArrayList;

public class SubcategoriesCrudListAdapter extends ArrayAdapter<Subcategory> {
    private ArrayList<Subcategory> aSubcategories;
    private SubcategoryRepository subcategoryRepository;

    public SubcategoriesCrudListAdapter(Context context, ArrayList<Subcategory> subcategories){
        super(context, R.layout.subcategory_card_crud, subcategories);
        aSubcategories = subcategories;

    }

    @Override
    public int getCount() {
        return aSubcategories.size();
    }


    @Nullable
    @Override
    public Subcategory getItem(int position) {
        return aSubcategories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Subcategory subcategory = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subcategory_card_crud,
                    parent, false);
        }
        LinearLayout categoryCard = convertView.findViewById(R.id.subcategory_card_item);
        TextView categoryName = convertView.findViewById(R.id.subcategory_name);
        TextView categoryDescription = convertView.findViewById(R.id.subcategory_description);
        TextView categorySubcategories = convertView.findViewById(R.id.subcategory_type);

        ImageButton editButton = convertView.findViewById(R.id.btnEdit);

        editButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + subcategory.getName() +
                    ", id: " + subcategory.getId());
            Intent intent = new Intent(getContext(), EditSubcategoryActivity.class);
            intent.putExtra("subcategory", subcategory);
            getContext().startActivity(intent);
        });

        if(subcategory != null){
            categoryName.setText(subcategory.getName());
            categoryDescription.setText(subcategory.getDescription());
            categorySubcategories.setText("Type: " + subcategory.getType());
            categoryCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + subcategory.getName() + ", id: " +
                        subcategory.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + subcategory.getName()  +
                        ", id: " + subcategory.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }
        ImageButton deleteButton = convertView.findViewById(R.id.btnDelete);


        subcategoryRepository = new SubcategoryRepository();
        deleteButton.setOnClickListener(v -> {
            Log.d("Klik", "klik");
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Are you sure you want to delete the subcategory " + subcategory.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            subcategory.setDeleted(true);
                            subcategoryRepository.updateSubcategory(subcategory);

                            aSubcategories.remove(subcategory);
                            notifyDataSetChanged();

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
