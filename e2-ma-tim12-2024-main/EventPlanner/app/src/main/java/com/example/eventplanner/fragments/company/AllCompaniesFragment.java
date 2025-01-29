package com.example.eventplanner.fragments.company;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CompanyListAdapter;
import com.example.eventplanner.adapters.PriceListAdapter;
import com.example.eventplanner.databinding.FragmentAllCompaniesBinding;
import com.example.eventplanner.databinding.FragmentServicesPricelistBinding;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.pricelist.PriceListItem;
import com.example.eventplanner.repositories.CompanyRepository;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.repositories.interfaces.ICompanyRepository;
import com.example.eventplanner.utils.PdfGenerator;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCompaniesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCompaniesFragment extends Fragment {

    private CompanyRepository companyRepository;
    private ArrayList<Company> allCompanies = new ArrayList<>();;
    private FragmentAllCompaniesBinding binding;

    public AllCompaniesFragment() {
        // Required empty public constructor
    }
    public static AllCompaniesFragment newInstance() {
        AllCompaniesFragment fragment = new AllCompaniesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllCompaniesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reloadData();

        return root;
    }

    public void reloadData() {
        companyRepository = new CompanyRepository();

        companyRepository.getAllCompanies(new ICompanyRepository.GetCompaniesCallback() {
            @Override
            public void OnGetCompanies(ArrayList<Company> companies) {
                ICompanyRepository.GetCompaniesCallback.super.OnGetCompanies(companies);
                if (companies != null) {
                    for (Company company : companies) {
                            allCompanies.add(company);
                        Log.d("CompanyInfo", "Company Name: " + company.getName() + ", Company Address: " + company.getCompanyAdress() + company.getOwnerName() + company.getOwnerLastname());
                    }
                    ListView listView = binding.companyListView;
                    CompanyListAdapter adapter = new CompanyListAdapter(requireContext(), allCompanies);
                    listView.setAdapter(adapter);
                }
            }
        });
    }
}