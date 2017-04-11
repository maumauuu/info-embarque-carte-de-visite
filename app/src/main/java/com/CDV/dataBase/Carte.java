package com.CDV.dataBase;



public class Carte {

    private long id;
    private String name;
    private String fullname;
    private String email;
    private String numero;
    private String address;
    private String city;
    private String postal;

    /*
     * getter
     */

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getFullname(){
        return fullname;
    }

    public String getEmail(){
        return email;
    }

    public String getNumero(){
        return numero;
    }

    public String getAddress(){
        return address;
    }

    public String getCity(){
        return city;
    }

    public String getPostal(){
        return postal;
    }

    /*
     *setter
     */

    public void setId(long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setNumero(String numero){
        this.numero = numero;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setPostal(String postal){
        this.postal = postal;
    }

    @Override
    public String toString(){
        return name+" "+fullname+" "+email+" "+numero+" "+address+" "+city+postal;
    }
}
