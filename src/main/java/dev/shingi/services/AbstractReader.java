package dev.shingi.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import dev.shingi.models.Customer;
import dev.shingi.models.CustomerList;
import dev.shingi.models.LedgerAccount;

public abstract class AbstractReader {

    /**
     * @return a CustomerList object.
     * @throws FileNotFoundException
     */
    public abstract CustomerList readCustomerData(boolean includeAccountComparator) throws FileNotFoundException;

    public void validateAccounts(List<LedgerAccount> accounts) {
        // Implementation for validation
    }

    public List<Customer> readClientNamesAndKeysFromFile() throws FileNotFoundException {
        List<Customer> customers = new ArrayList<>();

        Scanner sc = new Scanner(new File("RengerConnect/Client keys.txt"));
        Scanner lineScanner;

        while (sc.hasNextLine()) {
            String customerName;
            String clientKey = null;
            lineScanner = new Scanner(sc.nextLine());
            lineScanner.useDelimiter(" = ");

            customerName = lineScanner.next();
            if (lineScanner.hasNext()) {
                clientKey = lineScanner.next();
            }

            Customer customer = new Customer(customerName, clientKey);
            System.out.println(customer);

            if (customer.getClientKey() != null) {
                customers.add(customer);
            }
        }

        return customers;
    }
}