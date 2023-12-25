package dev.shingi.services;

import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.*;

import dev.shingi.API.endpoints.models.modelArrays.Grootboeken;
import dev.shingi.models.Customer;
import dev.shingi.models.CustomerList;
import dev.shingi.utils.SnelstartAuthentication;
import dev.shingi.utils.SnelstartReader;
import dev.shingi.utils.SnelstartUtils;

public class CustomerReaderSnelstart extends AbstractReader {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerReaderSnelstart.class);
    
    public CustomerList readCustomerData(boolean includeAccountComparator) throws FileNotFoundException {

        List<Customer> customers = readClientNamesAndKeysFromFile(); // Method in Abstractreader class

        int customerCount = 0;

        // Read info for customers
        for (Customer customer : customers) {
            customerCount++;
            
            if (customer.getClientKey() != null) {
                // Get bearer token
                String bearerToken = SnelstartAuthentication.getBearerToken(customer.getClientKey());

                // Set customer ledger accounts
                customer.setLedgerAccounts(SnelstartUtils.convertGrootboekenToLedgerAccounts(Grootboeken.readObjects(bearerToken)));

                // Set customer grootboekmutaties
                customer.setGrootboekMutaties(SnelstartReader.readGrootboekMutaties(bearerToken));
            } else {
                logger.info("Skipped " + customer + " - has no key.");
            }

            logger.info("Read " + customerCount + " of " + customers.size() + " customers.");
            
        }

        logger.info("Finished reading " + customerCount + " of " + customers.size() + " had keys to read ledger accounts.");

        return new CustomerList(customers, true);
    }
}
