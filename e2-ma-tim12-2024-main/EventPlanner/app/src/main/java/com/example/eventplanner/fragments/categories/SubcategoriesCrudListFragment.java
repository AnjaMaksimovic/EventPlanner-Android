package com.example.eventplanner.fragments.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CategoryCrudListAdapter;
import com.example.eventplanner.adapters.CategoryListAdapter;
import com.example.eventplanner.adapters.SubcategoriesCrudListAdapter;
import com.example.eventplanner.databinding.FragmentCategoriesCrudListBinding;
import com.example.eventplanner.databinding.FragmentSubcategoriesCrudListBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubcategoriesCrudListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubcategoriesCrudListFragment extends ListFragment {

    private SubcategoriesCrudListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Subcategory> mSubcategories;
    private FragmentSubcategoriesCrudListBinding binding;
    private SubcategoryRepository subcategoryRepository;
    public static SubcategoriesCrudListFragment newInstance(ArrayList<Subcategory> subcategories) {
        SubcategoriesCrudListFragment fragment = new SubcategoriesCrudListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, subcategories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subcategoryRepository = new SubcategoryRepository();
        subcategoryRepository.getAllSubcategories(new SubcategoryRepository.SubcategoryFetchCallback() {
            @Override
            public void onSubcategoryFetch(ArrayList<Subcategory> subcategories) {
                if (subcategories != null) {
                    ArrayList<Subcategory> filteredSubcategories = new ArrayList<>();
                    for (Subcategory subcategory : subcategories) {
                        if (!subcategory.getDeleted()) {
                            filteredSubcategories.add(subcategory);
                        }
                    }
                    adapter = new SubcategoriesCrudListAdapter(getActivity(), filteredSubcategories);
                    setListAdapter(adapter);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Subategory Crud List Fragment");
        binding = FragmentSubcategoriesCrudListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}