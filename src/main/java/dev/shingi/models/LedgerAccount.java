package dev.shingi.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LedgerAccount implements Comparable<LedgerAccount> {
    
    // Reference to original Grootboek model
    int nummer;
    String omschrijving;
    UUID id;
    String uri;

    double uniformityPercentage;
    List<Customer> customers;

    public LedgerAccount(String omschrijving, int nummer, UUID id, String uri) {
        this.omschrijving = omschrijving;
        this.nummer = nummer;
        this.id = id;
        this.uri = uri;

        uniformityPercentage = 0;
        customers = new ArrayList<>();
    }

    public LedgerAccount(String omschrijving) {
        this.omschrijving = omschrijving;

        uniformityPercentage = 0;
        customers = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerAccount that = (LedgerAccount) o;
        return Objects.equals(nummer, that.nummer) && Objects.equals(omschrijving, that.omschrijving);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nummer, omschrijving);
    }
    
    @Override
    public int compareTo(LedgerAccount other) {
        return Integer.compare(this.nummer, other.nummer);
    }
    
    @Override
    public String toString() {
        return omschrijving;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Integer getNummer() {
        return nummer;
    }

    public void setNummer(Integer nummer) {
        this.nummer = nummer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setUniformityPercentage(double i) {
        this.uniformityPercentage = i;
    }

    public double getUniformityPercentage() {
        return uniformityPercentage;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}