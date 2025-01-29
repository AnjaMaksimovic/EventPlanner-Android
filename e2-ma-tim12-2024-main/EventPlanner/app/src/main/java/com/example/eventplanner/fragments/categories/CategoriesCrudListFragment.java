package com.example.eventplanner.fragments.categories;

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
import com.example.eventplanner.adapters.CategoryCrudListAdapter;
import com.example.eventplanner.adapters.CategoryListAdapter;
import com.example.eventplanner.databinding.FragmentCategoriesCrudListBinding;
import com.example.eventplanner.databinding.FragmentCategoriesListBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.repositories.CategoryRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesCrudListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesCrudListFragment extends ListFragment {

    private CategoryCrudListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Category> mCategories;
    private FragmentCategoriesCrudListBinding binding;
    private CategoryRepository categoryRepo;

    public static CategoriesCrudListFragment newInstance(ArrayList<Category> categories) {
        CategoriesCrudListFragment fragment = new CategoriesCrudListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, categories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        categoryRepo = new CategoryRepository();
        categoryRepo.getAllCategories(new CategoryRepository.CategoryFetchCallback() {
            @Override
            public void onCategoryFetch(ArrayList<Category> categories) {
                if (categories != null) {
                    ArrayList<Category> filteredCategories = new ArrayList<>();
                    for (Category category : categories) {
                        if (!category.getDeleted()) {
                            filteredCategories.add(category);
                        }
                    }
                    adapter = new CategoryCrudListAdapter(getActivity(), filteredCategories);
                    setListAdapter(adapter);
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Category Crud List Fragment");
        binding = FragmentCategoriesCrudListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}