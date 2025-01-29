package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.products.ProductEditingActivity;
import com.example.eventplanner.model.BudgetItem;

import java.util.ArrayList;

public class BudgetItemListAdapter extends ArrayAdapter<BudgetItem> {

    private Context mContext;
    private ArrayList<BudgetItem> aBudgetItems;

    public BudgetItemListAdapter(Context context, ArrayList<BudgetItem> budgetItems){
        super(context, R.layout.budget_item_card, budgetItems);
        aBudgetItems = budgetItems;
        mContext = context;
    }

    @Override
    public int getCount() {
        return aBudgetItems.size();
    }


    @Nullable
    @Override
    public BudgetItem getItem(int position) {
        return aBudgetItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BudgetItem budgetItem = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.budget_item_card,
                    parent, false);
        }
        LinearLayout budgetItemCard = convertView.findViewById(R.id.budget_item_card_item);
        TextView budgeItemSubcategory = convertView.findViewById(R.id.budget_item_subcategory);
        TextView budgetItemPrice = convertView.findViewById(R.id.budget_item_price);
        ImageButton editButton = convertView.findViewById(R.id.btnEditBudgetItem);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDeleteBudgetItem);


        editButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + budgetItem.getSubcategory() +
                    ", id: " + budgetItem.getId());
            openEditBudgetItemDialog();
        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Are you sure you want to delete the item " + budgetItem.getSubcategory() + "?")
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


        if(budgetItem != null){
            budgeItemSubcategory.setText(budgetItem.getSubcategory());
            budgetItemPrice.setText("Price: " + budgetItem.getPrice());
            budgetItemCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
              //  Log.i("ShopApp", "Clicked: " + budgetItem.getSubcategory() + ", id: " +
              //          budgetItem.getId().toString());
             //   Toast.makeText(getContext(), "Clicked: " + category.getName()  +
              //          ", id: " + category.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }

    private void openEditBudgetItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.dialog_edit_budget_item, null);

        Button btnCancel = dialogView.findViewById(R.id.btnCancelEdit);
        Button btnOk = dialogView.findViewById(R.id.btnOkEdit);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
