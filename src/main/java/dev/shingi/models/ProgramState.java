package dev.shingi.models;

import java.util.ArrayList;
import java.util.List;

public class ProgramState {
    private static ProgramState instance = new ProgramState();

    private List<Runnable> xObject1Listeners = new ArrayList<>();
    private List<Runnable> xObjectListListeners = new ArrayList<>();

    private CustomerList customerList;
    private Customer customer;

    private ProgramState() {

    }

    public static ProgramState getInstance() {
        return instance;
    }

    public static void setInstance(ProgramState instance) {
        ProgramState.instance = instance;
    }

    public List<Runnable> getXObject1Listeners() {
        return xObject1Listeners;
    }

    public void setXObject1Listeners(List<Runnable> xObject1Listeners) {
        this.xObject1Listeners = xObject1Listeners;
    }

    public List<Runnable> getXObjectListListeners() {
        return xObjectListListeners;
    }

    public void setXObjectListListeners(List<Runnable> xObjectListListeners) {
        this.xObjectListListeners = xObjectListListeners;
    }

    public void notifyAllXObject1Listeners() {
        for (Runnable listener : xObject1Listeners) {
            listener.run(); // Notify each listener of the change
        }
    }

    public void notifyAllXObjectListListeners() {
        for (Runnable listener : xObjectListListeners) {
            listener.run(); // Notify each listener of the change
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        notifyAllXObject1Listeners();
    }

    public CustomerList getCustomerList() {
        return customerList;
    }

    public void setCustomerList(CustomerList customerList) {
        this.customerList = customerList;
        notifyAllXObjectListListeners();
    }
    
    
}
