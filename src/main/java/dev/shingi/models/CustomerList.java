package dev.shingi.models;

import java.util.*;

import dev.shingi.services.AccountComparator;

public class CustomerList {

    private List<Customer> customers;
    private AccountComparator accountComparator;

    private Map<Customer, Map<String, List<LedgerAccount>>> duplicateLedgerAccounts;
    private List<LedgerAccount> uniqueLedgerAccounts;
    private Map<String, List<LedgerAccount>> mismatchedLedgerAccounts;
    private List<LedgerAccount> uniformLedgerAccounts;

    public CustomerList(List<Customer> customers, boolean includeAccountComparator) {
        this.customers = customers;

        if (includeAccountComparator) {
            runAccountComparator();
        }
    }

    public void runAccountComparator() {
        this.accountComparator = new AccountComparator(this);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public AccountComparator getAccountComparator() {
        return accountComparator;
    }

    public void setAccountComparator(AccountComparator accountComparator) {
        this.accountComparator = accountComparator;
    }
    public Map<Customer, Map<String, List<LedgerAccount>>> getDuplicateLedgerAccounts() {
        return duplicateLedgerAccounts;
    }

    public void setDuplicateLedgerAccounts(Map<Customer, Map<String, List<LedgerAccount>>> duplicateLedgerAccounts) {
        this.duplicateLedgerAccounts = duplicateLedgerAccounts;
    }

    public List<LedgerAccount> getUniqueLedgerAccounts() {
        return uniqueLedgerAccounts;
    }

    public void setUniqueLedgerAccounts(List<LedgerAccount> uniqueLedgerAccounts) {
        this.uniqueLedgerAccounts = uniqueLedgerAccounts;
    }

    public Map<String, List<LedgerAccount>> getMismatchedLedgerAccounts() {
        return mismatchedLedgerAccounts;
    }

    public void setMismatchedLedgerAccounts(Map<String, List<LedgerAccount>> mismatchedLedgerAccounts) {
        this.mismatchedLedgerAccounts = mismatchedLedgerAccounts;
    }

    public List<LedgerAccount> getUniformLedgerAccounts() {
        return uniformLedgerAccounts;
    }

    public void setUniformLedgerAccounts(List<LedgerAccount> uniformLedgerAccounts) {
        this.uniformLedgerAccounts = uniformLedgerAccounts;
    }
}