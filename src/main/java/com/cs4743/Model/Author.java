package com.cs4743.Model;

import java.time.LocalDate;

public class Author {

    public int id = 0;
    public String firstName = "";
    public String lastName = "";
    public LocalDate dateOfBirth = null;
    public String gender = "";
    public String webSite = "";

    public Author() {
        id = this.getId();
        firstName = this.getFirstName();
        lastName = this.getLastName();
        dateOfBirth = this.getDateOfBirth();
        gender = this.getGender();
        webSite = this.getWebSite();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getWebSite() {
        return webSite;
    }
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}