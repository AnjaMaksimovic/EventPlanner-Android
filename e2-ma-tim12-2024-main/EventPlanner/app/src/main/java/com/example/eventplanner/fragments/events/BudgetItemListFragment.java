package com.example.eventplanner.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.BudgetItemListAdapter;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.databinding.FragmentBudgetItemListBinding;
import com.example.eventplanner.databinding.FragmentProductsListBinding;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.model.BudgetItem;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetItemListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetItemListFragment extends ListFragment {

    private BudgetItemListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<BudgetItem> mBudgetItems;
    private FragmentBudgetItemListBinding binding;

    public static BudgetItemListFragment newInstance(ArrayList<BudgetItem> budgetItems){
        BudgetItemListFragment fragment = new BudgetItemListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, budgetItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ShopApp", "onCreate BudgetItem List Fragment");
        if (getArguments() != null) {
            mBudgetItems = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new BudgetItemListAdapter(getActivity(), mBudgetItems);
            setListAdapter(adapter);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBudgetItemListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}