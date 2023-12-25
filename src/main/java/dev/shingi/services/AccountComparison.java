package dev.shingi.services;

import java.util.*;

import org.slf4j.*;

import dev.shingi.models.*;

public class AccountComparator {

    private static final Logger logger = LoggerFactory.getLogger(AccountComparator.class);

    public AccountComparator(CustomerList customers) {
        logger.info("START METHOD: Initializing AccountComparator");

        // Get all duplicate ledger accounts in the same customer for all customers
        customers.setDuplicateLedgerAccounts(identifyDuplicateLedgerAccounts(customers));

        // Get all ledger accounts, including setting the list of customers that has each ledger account
        List<LedgerAccount> allLedgerAccounts = getAllLedgerAccounts(customers);

        // Identify all ledger account with only one customer. Remove these from allLedgerAccounts and add it to uniqueLedgerAccounts
        customers.setUniqueLedgerAccounts(identifyUniqueLedgerAccounts(allLedgerAccounts));

        // Identify all ledger accounts with the same description but different number. Remove these from allLedgerAccounts and add them to mismatchedLedgerAccounts
        customers.setMismatchedLedgerAccounts(identifyMismatchedLedgerAccounts(allLedgerAccounts));

        // The remaining accounts in allLedgerAccounts are at least relatively uniform, having at least two associated customers
        customers.setUniformLedgerAccounts(allLedgerAccounts);
        logger.info("END METHOD: Uniform ledger accounts identified");

        computeUniformityPercentages(customers);
    }

    /**
     * Identifies duplicate LedgerAccounts within each Customer's accounts list based on account description.
     * 
     * @param customers The CustomerList to be analyzed.
     * @return A Map<Customer, Map<String, List<LedgerAccount>>>, where each Customer is mapped to a Map of 
     *         LedgerAccount descriptions and Lists of LedgerAccounts with those descriptions.
     *         Only includes Customers and descriptions with duplicates.
     */
    public Map<Customer, Map<String, List<LedgerAccount>>> identifyDuplicateLedgerAccounts(CustomerList customers) {
        logger.info("START METHOD: Identify duplicate ledger accounts for each customer in the customer list");
        Map<Customer, Map<String, List<LedgerAccount>>> internalDuplicates = new HashMap<>();
    
        // Loop through all customers
        for (Customer customer : customers.getCustomers()) {
            // First store all unique LedgerAccount descriptions in the accountMap key and all the LedgerAccounts with the same description the the value list
            Map<String, List<LedgerAccount>> accountMap = new HashMap<>();
            try { // Some customers do not have ledger accounts, so this will throw a NullPointerException
                for (LedgerAccount account : customer.getLedgerAccounts()) {
                    accountMap.computeIfAbsent(account.getOmschrijving(), k -> new ArrayList<>()).add(account);
                }
        
                // If the list contains more than one LedgerAccount, you have a duplicate. Add to internalDuplicates.
                for (Map.Entry<String, List<LedgerAccount>> entry : accountMap.entrySet()) {
                    if (entry.getValue().size() > 1) { // More than one account with the same description
                        internalDuplicates.computeIfAbsent(customer, k -> new HashMap<>()).put(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("END METHOD: Duplicate ledger accounts identified");
        return internalDuplicates;
    }

    private List<LedgerAccount> getAllLedgerAccounts(CustomerList customers) {
        logger.info("START METHOD: Getting all ledger accounts");
        List<LedgerAccount> allLedgerAccounts = new ArrayList<>();
    
        for (Customer customer : customers.getCustomers()) {
            logger.info("Processing customer: " + customer);
            if (customer.getLedgerAccounts() != null) {
                for (LedgerAccount account : customer.getLedgerAccounts()) {
                    if (account != null) {
                        logger.info("Processing account: " + account);
                        if (!allLedgerAccounts.contains(account)) {
                            allLedgerAccounts.add(account);
                            logger.info("Added new account: " + account);
                        }
                        allLedgerAccounts.get(allLedgerAccounts.indexOf(account)).getCustomers().add(customer);
                        logger.info("Added customer to account: " + account);
                    }
                }
            }
        }
        logger.info("END METHOD: All ledger accounts retrieved");
        return allLedgerAccounts;
    }    

    public List<LedgerAccount> identifyUniqueLedgerAccounts(List<LedgerAccount> allLedgerAccounts) {
        logger.info("START METHOD: Identifying unique ledger accounts");
        List<LedgerAccount> uniqueLedgerAccounts = new ArrayList<>();
        Iterator<LedgerAccount> iterator = allLedgerAccounts.iterator();
        while (iterator.hasNext()) {
            LedgerAccount ledgerAccount = iterator.next();
            logger.info("Checking account: " + ledgerAccount);
            if (ledgerAccount.getCustomers() != null && ledgerAccount.getCustomers().size() == 1) {
                uniqueLedgerAccounts.add(ledgerAccount);
                iterator.remove();
                logger.info("Added to unique accounts and removed from allLedgerAccounts: " + ledgerAccount);
            }
        }
        logger.info("END METHOD: Unique ledger accounts identified");
        return uniqueLedgerAccounts;
    }    

    private Map<String, List<LedgerAccount>> identifyMismatchedLedgerAccounts(List<LedgerAccount> allLedgerAccounts) {
        logger.info("START METHOD: Identifying mismatched ledger accounts");
        Map<String, List<LedgerAccount>> mismatchedLedgerAccounts = new HashMap<>();
    
        Map<String, List<LedgerAccount>> accountMap = new HashMap<>();
        for (LedgerAccount account : allLedgerAccounts) {
            logger.info("Processing account: " + account);
            accountMap.computeIfAbsent(account.getOmschrijving(), k -> new ArrayList<>()).add(account);
            logger.info("Added to accountMap: " + account);
        }
    
        Set<LedgerAccount> accountsToRemove = new HashSet<>();
    
        for (Map.Entry<String, List<LedgerAccount>> entry : accountMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                mismatchedLedgerAccounts.put(entry.getKey(), entry.getValue());
                accountsToRemove.addAll(entry.getValue());
                logger.info("Added mismatched accounts to removal list: " + entry.getValue());
            }
        }
    
        Iterator<LedgerAccount> iterator = allLedgerAccounts.iterator();
        while (iterator.hasNext()) {
            LedgerAccount currentAccount = iterator.next();
            if (accountsToRemove.contains(currentAccount)) {
                iterator.remove();
                logger.info("Removed mismatched account from allLedgerAccounts: " + currentAccount);
            }
        }
        logger.info("END METHOD: Mismatched ledger accounts identified");
        return mismatchedLedgerAccounts;
    }    

    private void computeUniformityPercentages(CustomerList customers) {
        logger.info("START METHOD: Computing uniformity percentages");
        List<LedgerAccount> ledgerAccounts = customers.getUniformLedgerAccounts();
        for (LedgerAccount ledgerAccount : ledgerAccounts) {
            if (ledgerAccount.getCustomers() != null) {
                double uniformityPercentage = ((double) ledgerAccount.getCustomers().size() / customers.getCustomers().size()) * 100;
                ledgerAccount.setUniformityPercentage(uniformityPercentage);
                logger.info("Set uniformity percentage for account: " + ledgerAccount + " to " + uniformityPercentage + "%");
            }
        }
        logger.info("END METHOD: Uniformity percentages computed");
    }
}