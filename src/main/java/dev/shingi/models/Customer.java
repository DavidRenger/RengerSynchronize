package dev.shingi.models;

import java.util.List;
public class Customer {
    
    private String name;
    private String clientKey;
    private List<LedgerAccount> ledgerAccounts;
    private List<GrootboekMutatie> grootboekMutaties;

    public Customer(String name, String clientKey) {
        this.name = name;
        this.clientKey = clientKey;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public List<LedgerAccount> getLedgerAccounts() {
        return ledgerAccounts;
    }

    public void setLedgerAccounts(List<LedgerAccount> ledgerAccounts) {
        this.ledgerAccounts = ledgerAccounts;
    }

    public List<GrootboekMutatie> getGrootboekMutaties() {
        return grootboekMutaties;
    }

    public void setGrootboekMutaties(List<GrootboekMutatie> grootboekMutaties) {
        this.grootboekMutaties = grootboekMutaties;
    }
}