package com.example.eventplanner.fragments.regRequest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.adapters.RegistrationRequestAdapter;
import com.example.eventplanner.databinding.FragmentAllRequestsListBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.repositories.CompanyRepository;
import com.example.eventplanner.repositories.interfaces.ICompanyRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AllRequestsListFragment extends ListFragment {

    private CompanyRepository companyRepository;
    private RegistrationRequestAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Person> mPeople;
    private FragmentAllRequestsListBinding binding;
    private static String searchText;
    private static String sorting;

    public AllRequestsListFragment() {}

    public static AllRequestsListFragment newInstance(ArrayList<Person> people, String query, String sort) {
        AllRequestsListFragment fragment = new AllRequestsListFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, people);
        args.putString("searchText", query);
        args.putString("sorting", sort);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");
        companyRepository = new CompanyRepository();

        Bundle args = getArguments();
        if (args != null) {
            searchText = args.getString("searchText");
            sorting = args.getString("sorting");

            if (searchText != null && !searchText.isEmpty()) {
                companyRepository.getAllCompaniesBySearch(searchText, new CompanyRepository.GetCompaniesCallback() {
                    @Override
                    public void OnGetCompanies(ArrayList<Company> companies) {
                        if (companies != null) {
                            AllRequestsFragment.backupCompanies = new ArrayList<>(companies);
                            sortAndDisplayCompanies(companies);
                        }
                    }
                });
            } else {
                companyRepository.getAllUnhandledCompanies(new ICompanyRepository.GetCompaniesCallback() {
                    @Override
                    public void OnGetCompanies(ArrayList<Company> companies) {
                        if (companies != null) {
                            AllRequestsFragment.backupCompanies = new ArrayList<>(companies);
                            sortAndDisplayCompanies(companies);
                        }
                    }
                });
            }
        }
    }

    private void sortAndDisplayCompanies(ArrayList<Company> companies) {
        // Provera da li su kompanije null ili prazne
        if (companies == null || companies.isEmpty()) {
            // Ako su null ili prazne, obavestite korisnika ili preduzmite odgovarajuće radnje
            Log.e("AllRequestsListFragment", "Companies list is null or empty");
            return;
        }

        // Filtriranje kompanija po izabranoj kategoriji i event tipu
        ArrayList<Company> filteredCompanies = new ArrayList<>();
        for (Company company : companies) {
            // Provera da li je kompanija null
            if (company == null) {
                // Ako je null, preskočite ovu iteraciju
                continue;
            }

            // Provera da li su kategorije kompanije null ili prazne
            if (company.getCategories() == null || company.getCategories().isEmpty()) {
                // Ako su null ili prazne, preskočite ovu iteraciju
                continue;
            }

            boolean passesCategoryFilter = true; // Inicijalno postavljamo na true
            boolean passesEventTypeFilter = true; // Inicijalno postavljamo na true

            // Provera da li kompanija zadovoljava kategoriju, ako je izabrana
            if (!AllRequestsFragment.selectedCategoryName.isEmpty()) {
                passesCategoryFilter = false; // Postavljamo na false jer trenutno nemamo kategoriju
                for (Category category : company.getCategories()) {
                    if (category != null && category.getName().equals(AllRequestsFragment.selectedCategoryName)) {
                        passesCategoryFilter = true; // Ako je kompanija zadovoljila kategoriju, postavljamo na true
                        break; // Ako je kompanija zadovoljila kategoriju, nema potrebe za daljom proverom
                    }
                }
            }

            // Provera da li kompanija zadovoljava event tip, ako je izabran
            if (!AllRequestsFragment.selectedEventTypeName.isEmpty()) {
                passesEventTypeFilter = false; // Postavljamo na false jer trenutno nemamo event tip
                for (EventType eventType : company.getEventTypes()) {
                    if (eventType != null && eventType.getName().equals(AllRequestsFragment.selectedEventTypeName)) {
                        passesEventTypeFilter = true; // Ako je kompanija zadovoljila event tip, postavljamo na true
                        break; // Ako je kompanija zadovoljila event tip, nema potrebe za daljom proverom
                    }
                }
            }

            // Ako kompanija zadovoljava oba filtera, dodajemo je u filtriranu listu
            if (passesCategoryFilter && passesEventTypeFilter) {
                filteredCompanies.add(company);
            }
        }

        // Sortiranje filtriranih kompanija
        if ("newest".equals(sorting)) {
            Collections.sort(filteredCompanies, new Comparator<Company>() {
                @Override
                public int compare(Company o1, Company o2) {
                    return o2.getCreateDate().compareTo(o1.getCreateDate());
                }
            });
        } else if ("oldest".equals(sorting)) {
            Collections.sort(filteredCompanies, new Comparator<Company>() {
                @Override
                public int compare(Company o1, Company o2) {
                    return o1.getCreateDate().compareTo(o2.getCreateDate());
                }
            });
        }

        // Postavljanje adaptera na listu filtriranih kompanija
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(filteredCompanies);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllRequestsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialize the adapter and set it to the ListView
        if (getActivity() != null) {
            adapter = new RegistrationRequestAdapter(getActivity(), new ArrayList<>());
            setListAdapter(adapter);
        } else {
            Log.e("AllRequestsListFragment", "getActivity() is null, cannot initialize adapter.");
        }
        return root;
    }
}
