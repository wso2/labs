package com.wso2.workshop.dao;

public class Customer {
    private String firstName;
    private String lastName;
    private String id;
    private Contact contact;

    public Customer(String firstName, String lastName, String id, Contact contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.contact = contact;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id='" + id + '\'' +
                ", contact=" + contact.toString() +
                '}';
    }
}
