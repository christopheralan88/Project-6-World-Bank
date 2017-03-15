package com.cj.worldbank;


import javax.persistence.*;

@Entity
public class Country {

    @Id // @Id annoation marks this field as the primary key
    // @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private String code;

    @Column
    private String name;

    @Column(name = "INTERNETUSERS")
    private Double internetUsers;

    @Column(name = "ADULTLITERACYRATE")
    private Double adultLiteracyRate;

    // default constructor so that JPA can utilize it
    public Country() {}

    public Country(String code, String name, Double internetUsers, Double adultLiteracyRate) {
        this.code = code;
        this.name = name;
        this.internetUsers = internetUsers;
        this.adultLiteracyRate = adultLiteracyRate;
    }

    public Country setCode(String code) {
        this.code = code;
        return this;
    }

    public Country setName(String name) {
        this.name = name;
        return this;
    }

    public Country setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
        return this;
    }

    public Country setAdultLiteracyRate(Double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
        return this;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Double getInternetUsers() {
        if (internetUsers != null) {
            return internetUsers;
        } else {
            return 0.00;
        }
    }

    public Double getAdultLiteracyRate() {
        if (adultLiteracyRate != null) {
            return adultLiteracyRate;
        } else {
            return 0.00;
        }
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }
}
