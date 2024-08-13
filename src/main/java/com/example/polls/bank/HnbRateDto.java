package com.example.polls.bank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HnbRateDto {

    @JsonProperty("broj_tecajnice")
    private String rateNumber;
    @JsonProperty("datum_primjene")
    private String date;
    @JsonProperty("drzava")
    private String country;
    @JsonProperty("drzava_iso")
    private String countryIso;
    @JsonProperty("kupovni_tecaj")
    private String buyingRate;
    @JsonProperty("prodajni_tecaj")
    private String middleRate;
    @JsonProperty("srednji_tecaj")
    private String sellingRate;
    @JsonProperty("sifra_valute")
    private String currencyCode;
    @JsonProperty("valuta")
    private String currency;

    public HnbRateDto() {
        //default constructor
    }

    public String getRateNumber() {
        return rateNumber;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public String getBuyingRate() {
        return buyingRate;
    }

    public String getMiddleRate() {
        return middleRate;
    }

    public String getSellingRate() {
        return sellingRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrency() {
        return currency;
    }
}