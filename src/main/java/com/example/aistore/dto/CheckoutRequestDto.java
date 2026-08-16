package com.example.aistore.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckoutRequestDto {
    private Long addressId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Street address is required")
    private String streetAddress;

    private String apartment;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country = "India";

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String paymentMethod = "CREDIT_CARD"; // CREDIT_CARD, UPI, COD, NET_BANKING
    private String cardNumber;
    private String upiId;

    public CheckoutRequestDto() {}

    public CheckoutRequestDto(Long addressId, String fullName, String streetAddress, String apartment, String city, String state, String postalCode, String country, String phone, String paymentMethod, String cardNumber, String upiId) {
        this.addressId = addressId;
        this.fullName = fullName;
        this.streetAddress = streetAddress;
        this.apartment = apartment;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country != null ? country : "India";
        this.phone = phone;
        this.paymentMethod = paymentMethod != null ? paymentMethod : "CREDIT_CARD";
        this.cardNumber = cardNumber;
        this.upiId = upiId;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long addressId;
        private String fullName;
        private String streetAddress;
        private String apartment;
        private String city;
        private String state;
        private String postalCode;
        private String country = "India";
        private String phone;
        private String paymentMethod = "CREDIT_CARD";
        private String cardNumber;
        private String upiId;

        public Builder addressId(Long addressId) { this.addressId = addressId; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder streetAddress(String streetAddress) { this.streetAddress = streetAddress; return this; }
        public Builder apartment(String apartment) { this.apartment = apartment; return this; }
        public Builder city(String city) { this.city = city; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder postalCode(String postalCode) { this.postalCode = postalCode; return this; }
        public Builder country(String country) { this.country = country; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder cardNumber(String cardNumber) { this.cardNumber = cardNumber; return this; }
        public Builder upiId(String upiId) { this.upiId = upiId; return this; }

        public CheckoutRequestDto build() {
            return new CheckoutRequestDto(addressId, fullName, streetAddress, apartment, city, state, postalCode, country, phone, paymentMethod, cardNumber, upiId);
        }
    }

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

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

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
}
