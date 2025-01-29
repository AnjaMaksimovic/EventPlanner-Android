package com.example.eventplanner.fragments.favourites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.products.ProductDetailsActivity;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.adapters.ServiceListAdapter;
import com.example.eventplanner.databinding.FragmentAllServicesListBinding;
import com.example.eventplanner.databinding.FragmentFavouriteProductListBinding;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.settings.PreferencesManager;
import com.itextpdf.layout.element.List;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteProductListFragment extends ListFragment {

    private String userId;
    private Role userRole;
    private PersonRepository personRepository;
    private ArrayList<Product> favouriteProducts;
    private Person person;
    private ProductListAdapter adapter;
    private static final String ARG_PARAM = "param";
   // private ArrayList<Service> mServices = new ArrayList<>();
    private FragmentFavouriteProductListBinding binding;

    public FavouriteProductListFragment() {
        // Required empty public constructor
    }

    public static FavouriteProductListFragment newInstance(ArrayList<Product> products) {
        FavouriteProductListFragment fragment = new FavouriteProductListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, products);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(getContext());
        userRole = PreferencesManager.getLoggedUserRole(getContext());

        if(userId != null) {
            personRepository = new PersonRepository();
            personRepository.getPersonByUserId(userId, new IPersonRepository.GetPersonsCallback() {
                @Override
                public void OnGetPeople(ArrayList<Person> people) {
                    if (people != null && !people.isEmpty()) {
                        person = people.get(0);
                        if (person != null) {
                            Log.d("UserProfileActivity", "Person found: " + person.toString());
                            if(person.getFavouriteProducts() != null) {
                                adapter = new ProductListAdapter(getActivity(), person.getFavouriteProducts(), userId, false, false, true, true);
                                setListAdapter(adapter);
                            }
                        } else {
                            Log.d("UserProfileActivity", "Person is null.");
                        }
                    } else {
                        Log.d("UserProfileActivity", "No person found with the given UserId.");
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteProductListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}