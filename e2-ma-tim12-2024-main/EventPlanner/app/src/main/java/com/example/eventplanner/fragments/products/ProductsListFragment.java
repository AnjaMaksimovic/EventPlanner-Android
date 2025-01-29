package com.example.eventplanner.fragments.products;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.databinding.FragmentProductsListBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.repositories.ProductRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsListFragment extends ListFragment {

    private ProductListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Product> mProducts;
    private FragmentProductsListBinding binding;
    private ProductRepository productRepo;
    private static String searchText;


    public static ProductsListFragment newInstance(ArrayList<Product> products, String query){
        ProductsListFragment fragment = new ProductsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, products);
        args.putString("searchText", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productRepo = new ProductRepository();

        Bundle args = getArguments();
        if(args != null) {
            searchText = args.getString("searchText");
            if (searchText != null) {
                productRepo.getAllProductsBySearchName(searchText, new ProductRepository.ProductFetchCallback() {
                    @Override
                    public void onProductFetch(ArrayList<Product> products) {
                        if (products != null) {
                            ArrayList<Product> filteredProducts = new ArrayList<>();
                            for (Product product : products) {
                                if (!product.isDeleted()) {
                                    filteredProducts.add(product);
                                }
                            }
                            adapter = new ProductListAdapter(getActivity(), filteredProducts, "", false, true, false, false);
                            setListAdapter(adapter);
                        }
                    }
                });
            } else if (searchText == "") {
                productRepo.getAllProducts(new ProductRepository.ProductFetchCallback() {
                    @Override
                    public void onProductFetch(ArrayList<Product> products) {
                        if (products != null) {
                            ArrayList<Product> filteredProducts = new ArrayList<>();
                            for (Product product : products) {
                                if (!product.isDeleted()) {
                                    filteredProducts.add(product);
                                }
                            }
                            adapter = new ProductListAdapter(getActivity(), filteredProducts, "",false, true, false, false);
                            setListAdapter(adapter);
                        }
                    }
                });
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}