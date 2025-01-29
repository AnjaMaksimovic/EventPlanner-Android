package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.packages.PackageEditingActivity;
import com.example.eventplanner.activities.products.ProductEditingActivity;
import com.example.eventplanner.model.Package;

import java.util.ArrayList;

public class PackageListAdapter extends ArrayAdapter<Package> {
    private ArrayList<Package> aPackages;

    private boolean CheckboxVisibility;
    private boolean ButtonsVisibility;

    public PackageListAdapter(Context context, ArrayList<Package> packages, boolean checkboxVisibility, boolean buttonsVisibility){
        super(context, R.layout.product_card, packages);
        aPackages = packages;
        CheckboxVisibility = checkboxVisibility;
        ButtonsVisibility = buttonsVisibility;
    }
    @Override
    public int getCount() {
        return aPackages.size();
    }


    @Nullable
    @Override
    public Package getItem(int position) {
        return aPackages.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Package listPackage = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.package_card,
                    parent, false);
        }
        LinearLayout packageCard = convertView.findViewById(R.id.package_card_item);
        ImageView imageView = convertView.findViewById(R.id.package_image);
        TextView packageName = convertView.findViewById(R.id.package_title);
        TextView packageDescription = convertView.findViewById(R.id.package_description);
        TextView packagePrice = convertView.findViewById(R.id.package_price);
        packagePrice.setTypeface(null, Typeface.BOLD);
        ImageButton editButton = convertView.findViewById(R.id.btnEditPackage);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDeletePackage);

        CheckBox checkBox = convertView.findViewById(R.id.package_item_checkbox);
        if (CheckboxVisibility) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        if (ButtonsVisibility) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

        editButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + listPackage.getName() +
                    ", id: " + listPackage.getId());
            Intent intent = new Intent(getContext(), PackageEditingActivity.class);
            intent.putExtra("productId", listPackage.getId());
            getContext().startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Are you sure you want to delete the package " + listPackage.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
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

        if(listPackage != null){
            imageView.setImageResource(listPackage.getImage());
            packageName.setText(listPackage.getName());
            packageDescription.setText(listPackage.getDescription());
            packagePrice.setText("Price: " + listPackage.getPrice());
            packageCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + listPackage.getName() + ", id: " +
                        listPackage.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + listPackage.getName()  +
                        ", id: " + listPackage.getId().toString(), Toast.LENGTH_SHORT).show();

            });
        }

        return convertView;
    }

}
