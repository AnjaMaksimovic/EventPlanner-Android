package com.example.eventplanner.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.HomeActivity;
import com.example.eventplanner.activities.pricelist.PricelistEditingActivity;
import com.example.eventplanner.activities.products.ProductEditingActivity;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.model.pricelist.PriceListItem;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PriceListAdapter extends ArrayAdapter<PriceListItem> {

    private ArrayList<PriceListItem> aPricelistItems;
    private Role userRole;

    public PriceListAdapter(Context context, ArrayList<PriceListItem> items){
        super(context, R.layout.price_list_item, items);
        aPricelistItems = items;
    }

    @Override
    public int getCount() {
        return aPricelistItems.size();
    }


    @Nullable
    @Override
    public PriceListItem getItem(int position) {
        return aPricelistItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PriceListItem item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.price_list_item,
                    parent, false);
        }
        TextView orderNumber = convertView.findViewById(R.id.order_number);
        TextView itemName = convertView.findViewById(R.id.name);
        TextView itemPrice = convertView.findViewById(R.id.price);
        TextView itemDiscount = convertView.findViewById(R.id.discount);
        TextView itemPriceWithDiscount = convertView.findViewById(R.id.price_with_discount);


        userRole = PreferencesManager.getLoggedUserRole(getContext());

        if (userRole != null && userRole.equals(Role.owner)) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PricelistEditingActivity.class);
                    if (item.getItem() instanceof Product) {
                        Product product = (Product) item.getItem();
                        intent.putExtra("product", product);
                        getContext().startActivity(intent);
                    } else if (item.getItem() instanceof Service) {
                        Service service = (Service) item.getItem();
                        intent.putExtra("service", service);
                        getContext().startActivity(intent);
                    } else if (item.getItem() instanceof Package) {
                        Package package1 = (Package) item.getItem();
                        intent.putExtra("package", package1);
                        Log.d("PackageContents", "Discount before activity: " + package1.getDiscount());
                        getContext().startActivity(intent);
                    }
                    updatePricelistItem(item);
                    notifyDataSetChanged();
                }
            });
        }

        if(item != null){
            itemName.setText(item.getItem().getName());
            itemPrice.setText("" + item.getItem().getPrice());
            itemDiscount.setText("" + item.getItem().getDiscount());
            itemPriceWithDiscount.setText("" + item.getItem().getPriceWithDiscount());
            orderNumber.setText("" + item.getOrderNumber());
        }

        return convertView;
    }
    public void updatePricelistItem(PriceListItem updatedItem) {
        for (int i = 0; i < aPricelistItems.size(); i++) {
            PriceListItem item = aPricelistItems.get(i);
            if (item.getOrderNumber() == updatedItem.getOrderNumber()) {
                aPricelistItems.set(i, updatedItem);
                notifyDataSetChanged();
                break;
            }
        }
    }
}

