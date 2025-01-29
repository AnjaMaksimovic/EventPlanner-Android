package com.example.eventplanner.fragments.events;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentEventBudgetBinding;
import com.example.eventplanner.databinding.FragmentEventPageBinding;
import com.example.eventplanner.databinding.FragmentProductsPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.categories.CategoriesListFragment;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.model.BudgetItem;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventBudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventBudgetFragment extends Fragment {

    private String eventId;
    public static ArrayList<Category> categories = new ArrayList<Category>();
    public static ArrayList<BudgetItem> budgetItems = new ArrayList<BudgetItem>();
    FragmentEventBudgetBinding binding;

    public EventBudgetFragment() {
        // Required empty public constructor
    }

    public static EventBudgetFragment newInstance(String eventId) {
        EventBudgetFragment fragment = new EventBudgetFragment();
        Bundle args = new Bundle();
        args.putString("eventId", eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventBudgetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareCategoriesList(categories);
        prepareBudgetItemsList(budgetItems);

        binding.btnAddNewItem.setOnClickListener(v -> {
            showAddBudgetItemDialog();
        });

        FragmentTransition.to(CategoriesListFragment.newInstance(categories), getActivity(),
                false, R.id.scroll_categories_list);

        FragmentTransition.to(BudgetItemListFragment.newInstance(budgetItems), getActivity(),
                false, R.id.scroll_budget_items_list);

        return root;
    }

    private void prepareCategoriesList(ArrayList<Category> categories){
        categories.add(new Category("1L", "Photo and video", "Professional photography and event filming.", false));
        categories.add(new Category("2L", "Catering", "Professional catering services for events and gatherings.", false));
    }

    private void prepareBudgetItemsList(ArrayList<BudgetItem> budgetItems){
        budgetItems.add(new BudgetItem(1L, "Photography Services", 500));
        budgetItems.add(new BudgetItem(1L, "Photos and Albums", 1000));
        budgetItems.add(new BudgetItem(1L, "Menu Planning", 700));
    }

    private void showAddBudgetItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_budget_item, null);

      //  final EditText etSubcategoryName = dialogView.findViewById(R.id.etSubcategoryName);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

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