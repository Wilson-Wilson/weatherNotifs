package com.example.particulartech;

/**
 * Created by jawil on 12/3/2017.
 */

public class Database {
    private Person [] workforce = new Person[5];//when testing set array length to 5
    private String [] names = {"Bob", "Alice"};
    private String [] roles = {"Manufacturer","IT"};
    private String [] email = {"technalysis1@gmail.com", "technalysis1@hotmail.com", "technalysis6@gmail.com",
            "zdoctour@gmail.com", "zdoctour@hotmail.com"};//more emails would be necessary for large scale testing.

    public Database(){
        /*int i=0;
        for(;i < 20; i++){
            workforce[i] = new Person(names[(int)(Math.random()*2)], "address"+i, "Montego Bay",
                    "Jamaica", 8769876, roles[(int)(Math.random()*2)], email[(int)(Math.random()*3)]);
        }

        for(;i < 30; i++){
            workforce[i] = new Person(names[(int)(Math.random()*2)], "address"+i, "Kingston",
                    "Jamaica", 8769876, roles[(int)(Math.random()*2)], email[3 + (int)(Math.random() * ((4 - 3) + 1))]);
        }*/


        //used for small scall testing purposes
        workforce[0] = new Person(names[(int)(Math.random()*2)], "address1", "Montego Bay",
                "Jamaica", 8769876, roles[(int)(Math.random()*2)], email[0]);
        workforce[1] = new Person(names[(int)(Math.random()*2)], "address2", "Montego Bay",
                "Jamaica", 8769876, roles[(int)(Math.random()*2)], email[1]);
        workforce[2] = new Person(names[(int)(Math.random()*2)], "address3", "Kingston",
                "Jamaica", 8769876, roles[(int)(Math.random()*2)], email[2]);
        workforce[3] = new Person(names[(int)(Math.random()*2)], "address4", "Kingston",
                "Jamaica", 8769876, roles[(int)(Math.random()*2)], email[3]);
        workforce[4] = new Person(names[(int)(Math.random()*2)], "address5", "Kingston",
                "Jamaica", 8769876, roles[(int)(Math.random()*2)], email[4]);
    }

    public Person[] getWorkforce(){
        return workforce;
    }
}
