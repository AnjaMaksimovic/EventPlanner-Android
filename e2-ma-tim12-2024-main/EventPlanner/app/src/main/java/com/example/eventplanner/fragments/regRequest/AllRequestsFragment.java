package com.example.eventplanner.fragments.regRequest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.categories.CreateSubcategoryActivity;
import com.example.eventplanner.adapters.CategoryNameAdapter;
import com.example.eventplanner.adapters.EventTypeNameAdapter;
import com.example.eventplanner.databinding.FragmentAllRequestsBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationStatus;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.EventTypeRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;

import java.util.ArrayList;

public class AllRequestsFragment extends Fragment {

    public static ArrayList<Person> people = new ArrayList<>();
    public static ArrayList<Company> backupCompanies = new ArrayList<>();
    private FragmentAllRequestsBinding binding;
    private PersonRepository personRepository;
    private SearchView searchView;
    private CheckBox cbNewest, cbOldest, cbNoFilterCat, cbNoFilterEventType;
    private Spinner categorySpinner;
    public static String selectedCategoryName = "";

    private Spinner eventTypeSpinner;
    public static String selectedEventTypeName = "";
    private CategoryRepository categoryRepository;
    private EventTypeRepository eventTypeRepository;

    public AllRequestsFragment() {}

    public static AllRequestsFragment newInstance(String param1, String param2) {
        AllRequestsFragment fragment = new AllRequestsFragment();
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
        binding = FragmentAllRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        searchView = binding.searchText;
        String searchText = "";

        FragmentTransition.to(AllRequestsListFragment.newInstance(people, searchText, "newest"), getActivity(),
                false, R.id.scroll_products_list);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updatePeopleList(query, "newest");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    updatePeopleList("", "newest");
                }
                return false;
            }
        });

        categorySpinner = binding.spinCategory;
        categoryRepository = new CategoryRepository();
        ArrayList<Category> categories = new ArrayList<>();
        categoryRepository.getAllCategories(new CategoryRepository.CategoryFetchCallback(){
            @Override
            public void onCategoryFetch(ArrayList<Category> fetchedCategories) {
                if (fetchedCategories != null){
                    categories.addAll(fetchedCategories);
                    CategoryNameAdapter categoryAdapter = new CategoryNameAdapter(getContext(), android.R.layout.simple_spinner_item, categories);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    categorySpinner.setAdapter(categoryAdapter);

                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Dobijanje izabrane kategorije
                            Category selectedCategory = categories.get(position);

                            // Postavljanje imena selektovane kategorije u static varijablu
                            selectedCategoryName = selectedCategory.getName();

                            // Pozivanje metode za ažuriranje liste na osnovu izabrane kategorije
                            updatePeopleList(searchView.getQuery().toString(), "newest");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Implementacija po potrebi
                        }
                    });
                }
            }
        });

        // Dodajte spinner za odabir event tipa
        eventTypeSpinner = binding.spinEventTypes;

// Inicijalizacija EventTypeRepository
        eventTypeRepository = new EventTypeRepository();

// Lista za čuvanje event tipova
        ArrayList<EventType> eventTypes = new ArrayList<>();

// Poziv metode za dobijanje svih event tipova iz baze podataka
        eventTypeRepository.getAllEventTypes(new EventTypeRepository.EventTypeFetchCallback(){
            @Override
            public void onEventTypeFetch(ArrayList<EventType> fetchedEventTypes) {
                if (fetchedEventTypes != null){
                    eventTypes.addAll(fetchedEventTypes);
                    EventTypeNameAdapter eventTypeAdapter = new EventTypeNameAdapter(getContext(), android.R.layout.simple_spinner_item, eventTypes);
                    eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Postavljanje adaptera na spinner
                    eventTypeSpinner.setAdapter(eventTypeAdapter);

                    // Dodavanje slušača za promene u selektovanom event tipu
                    eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Dobijanje izabranog event tipa
                            EventType selectedEventType = eventTypes.get(position);

                            // Postavljanje imena selektovanog event tipa u static varijablu
                            selectedEventTypeName = selectedEventType.getName();

                            // Pozivanje metode za ažuriranje liste na osnovu izabranog event tipa
                            updatePeopleList(searchView.getQuery().toString(), "newest");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Implementacija po potrebi
                        }
                    });
                }
            }
        });


        // Inicijalizacija CheckBox-a za filtere
        initializeFiltering();

        return root;
    }

    private void initializeFiltering() {
        cbNewest = binding.cbNewest;
        cbOldest = binding.cbOldest;
        cbNoFilterCat = binding.cbNoFilterCat;
        cbNoFilterEventType = binding.cbNoFilterEventType;

        // Postavljanje slušača za promene stanja CheckBox-eva
        CompoundButton.OnCheckedChangeListener checkBoxListener = (buttonView, isChecked) -> {
            if (isChecked) {
                // Ako je Newest odabran, onemogući Oldest
                if (buttonView.getId() == R.id.cbNewest) {
                    cbOldest.setChecked(false);
                    updatePeopleList(searchView.getQuery().toString(), "newest");
                }
                // Ako je Oldest odabran, onemogući Newest
                else if (buttonView.getId() == R.id.cbOldest) {
                    cbNewest.setChecked(false);
                    updatePeopleList(searchView.getQuery().toString(), "oldest");
                }
                // Ako je No Filter Category odabran, postavi selectedCategoryName na prazan string
                else if (buttonView.getId() == R.id.cbNoFilterCat) {
                    selectedCategoryName = "";
                    // Sakrij spiner sa kategorijama
                    categorySpinner.setVisibility(View.GONE);
                    updatePeopleList(searchView.getQuery().toString(), "newest");
                }
                // Ako je No Filter Event Type odabran, postavi selectedEventTypeName na prazan string
                else if (buttonView.getId() == R.id.cbNoFilterEventType) {
                    selectedEventTypeName = "";
                    // Sakrij spiner sa event tipovima
                    eventTypeSpinner.setVisibility(View.GONE);
                    updatePeopleList(searchView.getQuery().toString(), "newest");
                }
            }
            // Ako nijedan nije odabran, prikaži početno stanje (Newest)
            else {
                // Ako je No Filter Category odznačen, ponovo prikaži spiner sa kategorijama
                if (buttonView.getId() == R.id.cbNoFilterCat) {
                    categorySpinner.setVisibility(View.VISIBLE);
                }
                // Ako je No Filter Event Type odznačen, ponovo prikaži spiner sa event tipovima
                else if (buttonView.getId() == R.id.cbNoFilterEventType) {
                    eventTypeSpinner.setVisibility(View.VISIBLE);
                }
                updatePeopleList(searchView.getQuery().toString(), "newest");
            }
        };

        cbNewest.setOnCheckedChangeListener(checkBoxListener);
        cbOldest.setOnCheckedChangeListener(checkBoxListener);
        cbNoFilterCat.setOnCheckedChangeListener(checkBoxListener);
        cbNoFilterEventType.setOnCheckedChangeListener(checkBoxListener);


    }

    private void updatePeopleList(String query, String sort){

        FragmentTransition.to(AllRequestsListFragment.newInstance(people, query, sort), getActivity(),
                false, R.id.scroll_products_list);
    }
}
