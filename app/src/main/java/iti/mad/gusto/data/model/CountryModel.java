package iti.mad.gusto.data.model;

public class CountryModel {
    private String strArea;

    public String getStrArea(){
        return strArea;
    }

    @Override
    public String toString() {
        return "CountriesItem{" +
                "strArea='" + strArea + '\'' +
                '}';
    }
}