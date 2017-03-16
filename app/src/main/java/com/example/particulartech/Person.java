package com.example.particulartech;

/**
 * Created by jawil on 12/3/2017.
 */

public class Person {
    private String name, address1, city, country, role, email;
    private  int telephoneNo;

    public Person(String name, String address1, String city, String country, int telephoneNo, String role, String email){
        this.name = name; this.address1 = address1;
        this.city = city; this.country = country;
        this.role = role; this.email = email;
        this.telephoneNo = telephoneNo;
    }

    public String getCity(){
        return city;
    }

    public String getEmail(){
        return email;
    }
}
