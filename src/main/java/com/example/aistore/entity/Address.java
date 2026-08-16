package com.example.aistore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String streetAddress;

    private String apartment;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    private String phone;

    private boolean isDefault;

    private String addressType; // HOME, WORK, OTHER

    public Address() {}

    public Address(Long id, User user, String fullName, String streetAddress, String apartment, String city, String state, String postalCode, String country, String phone, boolean isDefault, String addressType) {
        this.id = id;
        this.user = user;
        this.fullName = fullName;
        this.streetAddress = streetAddress;
        this.apartment = apartment;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.isDefault = isDefault;
        this.addressType = addressType;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String fullName;
        private String streetAddress;
        private String apartment;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String phone;
        private boolean isDefault;
        private String addressType;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder streetAddress(String streetAddress) { this.streetAddress = streetAddress; return this; }
        public Builder apartment(String apartment) { this.apartment = apartment; return this; }
        public Builder city(String city) { this.city = city; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder postalCode(String postalCode) { this.postalCode = postalCode; return this; }
        public Builder country(String country) { this.country = country; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder isDefault(boolean isDefault) { this.isDefault = isDefault; return this; }
        public Builder addressType(String addressType) { this.addressType = addressType; return this; }

        public Address build() {
            return new Address(id, user, fullName, streetAddress, apartment, city, state, postalCode, country, phone, isDefault, addressType);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
}
