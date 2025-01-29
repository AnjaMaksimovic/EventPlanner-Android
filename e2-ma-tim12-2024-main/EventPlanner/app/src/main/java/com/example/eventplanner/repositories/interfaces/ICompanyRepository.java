package com.example.eventplanner.repositories.interfaces;

import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.User;

import java.util.ArrayList;

public interface ICompanyRepository {
    public void Create(Company company);
    public void Delete(String companyId);
    void Update(Company company);

    void GetByOwnerId(String ownerId, GetCompaniesCallback callback);

    public interface GetCompaniesCallback{
        default void OnGetCompanies(ArrayList<Company> companies) {}
        default void onResult(boolean contains) {}
    }

}