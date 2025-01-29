package com.example.eventplanner.fragments.products;

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
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.databinding.FragmentAllProductsListBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllProductsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllProductsListFragment extends ListFragment {

    private String userId;
    private Role userRole;
    private ProductRepository productRepo;
    private ProductListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Product> mProducts;
    private FragmentAllProductsListBinding binding;
    private static String searchText;

    public static AllProductsListFragment newInstance(ArrayList<Product> products, String query) {
        AllProductsListFragment fragment = new AllProductsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, products);
        args.putString("searchText", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(getContext());
        userRole = PreferencesManager.getLoggedUserRole(getContext());

        Log.i("ShopApp", "onCreate Products List Fragment");
        productRepo = new ProductRepository();

        Bundle args = getArguments();
        if(args != null) {
            searchText = args.getString("searchText");
            if (searchText != null) {

                if(userRole == Role.employee || userRole == Role.admin || userRole == Role.owner) {
                    productRepo.getAllProductsBySearchName(searchText, new ProductRepository.ProductFetchCallback() {
                        @Override
                        public void onProductFetch(ArrayList<Product> products) {
                            if (products != null) {
                                adapter = new ProductListAdapter(getActivity(), products, "", false, false, true, false);
                                setListAdapter(adapter);
                            }
                        }
                    });
                }else {
                    productRepo.getAllVisibleProductsBySearchName(searchText, new ProductRepository.ProductFetchCallback() {
                        @Override
                        public void onProductFetch(ArrayList<Product> products) {
                            if (products != null) {
                                adapter = new ProductListAdapter(getActivity(), products, "", false, false, true, false);
                                setListAdapter(adapter);
                            }
                        }
                    });
                }
            } else if (searchText == "") {

                if(userRole == Role.employee || userRole == Role.admin || userRole == Role.owner) {
                    productRepo.getAllProducts(new ProductRepository.ProductFetchCallback() {
                        @Override
                        public void onProductFetch(ArrayList<Product> products) {
                            if (products != null) {
                                adapter = new ProductListAdapter(getActivity(), products, "", false, false, true, false);
                                setListAdapter(adapter);
                            }
                        }
                    });
                } else {
                    productRepo.getAllVisibleProducts(new ProductRepository.ProductFetchCallback() {
                        @Override
                        public void onProductFetch(ArrayList<Product> products) {
                            if (products != null) {
                                adapter = new ProductListAdapter(getActivity(), products, "",false, false, true, false);
                                setListAdapter(adapter);
                            }
                        }
                    });
                }
            }
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllProductsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}