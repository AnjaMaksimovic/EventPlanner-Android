package com.example.eventplanner.repositories.interfaces;

import com.example.eventplanner.model.Person;

import java.util.ArrayList;

public interface IPersonRepository {
    public void Create(Person person);
    public void Delete(String personId);
    public void Update(String personId, Person person);
    public void Get(String personId, GetPersonsCallback callback);

    void getPersonByUserId(String userId, GetPersonsCallback callback);

    public void GetAllByCompany(String companyId, GetPersonsCallback callback);
    public void SearchPeopleByCompany(String companyId, String name, String lastname, String email, GetPersonsCallback callback);

    public interface GetPersonsCallback{
        default void OnGetPeople(ArrayList<Person> persons) {}
        default void OnResult(boolean contains) {}
    }
}