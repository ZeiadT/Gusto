package iti.mad.gusto.data.model;

import java.util.List;

public class CountryResponse{
    private List<CountryModel> meals;

    public List<CountryModel> getCountries(){
        return meals;
    }
}