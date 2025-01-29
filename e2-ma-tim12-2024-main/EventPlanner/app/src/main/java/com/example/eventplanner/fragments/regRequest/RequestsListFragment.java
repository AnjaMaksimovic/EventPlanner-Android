package com.example.eventplanner.fragments.regRequest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.adapters.RegistrationRequestAdapter;
import com.example.eventplanner.databinding.FragmentRequestsListBinding;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.repositories.CompanyRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.interfaces.ICompanyRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";

    private RegistrationRequestAdapter adapter;
    private ArrayList<Person> mPeople;
    private FragmentRequestsListBinding binding;
    //private PersonRepository personRepository;
    private CompanyRepository companyRepository;
    private static String searchText;

    public RequestsListFragment() {
    }

    public static RequestsListFragment newInstance(ArrayList<Person> people, String query) {
        RequestsListFragment fragment = new RequestsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, people);
        args.putString("searchText", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //personRepository = new PersonRepository();
        companyRepository = new CompanyRepository();

        /*Bundle args = getArguments();
        if(args != null) {
            searchText = args.getString("searchText");
            if (searchText != null) {
                companyRepository.getAllCompaniesBySearch(searchText, new CompanyRepository.GetCompaniesCallback() {
                    @Override
                    public void OnGetCompanies(ArrayList<Company> companies) {
                        if (companies != null) {
                            ArrayList<Company> filteredCompanies = new ArrayList<>();
                            for (Company company : companies) {
                                if (!company.getRequestHandled()) {
                                    filteredCompanies.add(company);
                                }
                            }
                            adapter = new RegistrationRequestAdapter(getActivity(), filteredCompanies);
                            setListAdapter(adapter);
                        }
                    }
                });
            } else if (searchText == "") {
                companyRepository.getAllUnhandledCompanies(new ICompanyRepository.GetCompaniesCallback() {
                    @Override
                    public void OnGetCompanies(ArrayList<Company> companies) {
                        if (companies != null) {
                            ArrayList<Company> filteredCompanies = new ArrayList<>();
                            for (Company company : companies) {
                                if (company.getRequestHandled() == false) {
                                    filteredCompanies.add(company);
                                }
                            }
                            adapter = new RegistrationRequestAdapter(getActivity(), filteredCompanies);
                            setListAdapter(adapter);
                        }
                    }
                });
            }
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests_list, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}