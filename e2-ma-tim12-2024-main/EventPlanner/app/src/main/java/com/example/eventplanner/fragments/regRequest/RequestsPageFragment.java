package com.example.eventplanner.fragments.regRequest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentProductsPageBinding;
import com.example.eventplanner.databinding.FragmentRequestsPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.repositories.PersonRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsPageFragment extends Fragment {

    public static ArrayList<Person> people = new ArrayList<>();
    private FragmentRequestsPageBinding binding;
    private PersonRepository personRepository;
    private SearchView searchView;
    public RequestsPageFragment() {
        // Required empty public constructor
    }

    public static RequestsPageFragment newInstance() {
        RequestsPageFragment fragment = new RequestsPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRequestsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = binding.searchText;

        String searchText = "";
        FragmentTransition.to(RequestsListFragment.newInstance(people, searchText), getActivity(),
                false, R.id.scroll_products_list);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submit
                updatePeopleList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    updatePeopleList("");
                }
                return false;
            }
        });
        return root;
    }

    private void updatePeopleList(String query) {
        FragmentTransition.to(RequestsListFragment.newInstance(people, query), getActivity(),
                false, R.id.scroll_products_list);
    }
}