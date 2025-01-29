package com.example.eventplanner.fragments.stakeholders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.PeopleAdapter;
import com.example.eventplanner.databinding.FragmentEmployeesBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;

import java.util.ArrayList;

public class EmployeesFragment extends ListFragment {


    public static ArrayList<Person> people = new ArrayList<Person>();
    private PersonRepository personRepository;
    private PeopleAdapter adapter;
    private FragmentEmployeesBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployeesFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static EmployeesFragment newInstance(String param1, String param2) {
        EmployeesFragment fragment = new EmployeesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        personRepository = new PersonRepository();
        preparePeopleList();
        adapter = new PeopleAdapter(getActivity(), people, new PeopleAdapter.OnPersonClickListener() {
            @Override
            public void OnPersonClick(Person person) {
                FragmentTransition.to(DetailsFragment.newInstance(person), getActivity(), false, R.id.fragment_nav_content_main);
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false);

        binding.registerEmployeeBtn.setOnClickListener(v -> {
            FragmentTransition.to(EmployeeRegistrationFragment.newInstance("", ""), getActivity(),
                    false, R.id.fragment_nav_content_main);
        });

        binding.searchEmployeesBtn.setOnClickListener(v -> {
            SearchEmployess();
        });
        return binding.getRoot();
    }

    private void SearchEmployess() {
        people.clear();
        adapter.clear();

        String name = binding.searchName.getText().toString().trim();
        String lastname = binding.searchLastname.getText().toString().trim();
        String email = binding.searchEmail.getText().toString().trim();

        if(name.isEmpty() && lastname.isEmpty() && email.isEmpty()){
            preparePeopleList();
        }
        else{
            personRepository.SearchPeopleByCompany("Company_1", name, lastname, email, new IPersonRepository.GetPersonsCallback() {
                @Override
                public void OnGetPeople(ArrayList<Person> persons) {
                    IPersonRepository.GetPersonsCallback.super.OnGetPeople(persons);
                    if(persons.isEmpty()){
                        adapter.notifyDataSetChanged(); return;
                    }
                    people.addAll(persons);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void preparePeopleList(){
        people.clear();
        personRepository.GetAllByCompany("Company_1", new IPersonRepository.GetPersonsCallback() {
            @Override
            public void OnGetPeople(ArrayList<Person> peopleList) {
                IPersonRepository.GetPersonsCallback.super.OnGetPeople(peopleList);
                if(peopleList.isEmpty()) return;
                people.addAll(peopleList);
                adapter.notifyDataSetChanged();
            }
        });
    }
}