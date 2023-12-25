package dev.shingi.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dev.shingi.models.Customer;
import dev.shingi.models.LedgerAccount;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelExporter {

    public void exportComparisonsToExcel(
        Map<Customer, Map<String, List<LedgerAccount>>> duplicateLedgerAccounts,
        List<LedgerAccount> uniqueLedgerAccounts,
        Map<String, List<LedgerAccount>> mismatchedLedgerAccounts,
        List<LedgerAccount> uniformLedgerAccounts,
        String filePath) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            // Create sheets
            Sheet duplicatesSheet = workbook.createSheet("Duplicates Accounts");
            Sheet uniqueSheet = workbook.createSheet("Unique Accounts");
            Sheet mismatchedSheet = workbook.createSheet("Mismatched Accounts");
            Sheet uniformSheet = workbook.createSheet("Uniform Accounts");

            // Populate the Duplicate Accounts sheet
            populateDuplicatesSheet(duplicatesSheet, duplicateLedgerAccounts);

            // Populate the Unique Accounts sheet
            populateUniqueSheet(uniqueSheet, uniqueLedgerAccounts);

            // Populate the Mismatched Accounts sheet
            populateMismatchedSheet(mismatchedSheet, mismatchedLedgerAccounts);

            // Populate the Uniform Accounts sheet
            populateUniformSheet(uniformSheet, uniformLedgerAccounts);

            // Write the workbook to a file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("Finished writing results.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Program failed to write results.");
            }
        }
    }

    private void populateDuplicatesSheet(Sheet sheet, Map<Customer, Map<String, List<LedgerAccount>>> duplicateLedgerAccounts) {
        int rowNum = 0;
        // Loop through customers
        for (Map.Entry<Customer, Map<String, List<LedgerAccount>>> customerEntry : duplicateLedgerAccounts.entrySet()) {
            Customer customer = customerEntry.getKey();
            Map<String, List<LedgerAccount>> accounts = customerEntry.getValue();

            // Create customer cell
            sheet.createRow(rowNum++).createCell(0).setCellValue(customer.getName());
            // System.out.println(customer.getName() + " has duplicate ledger accounts:");
    
            // Loop through duplicate ledger accounts
            for (Map.Entry<String, List<LedgerAccount>> accountEntry : accounts.entrySet()) {
                List<LedgerAccount> ledgerAccounts = accountEntry.getValue();
    
                for (LedgerAccount account : ledgerAccounts) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(1).setCellValue(account.getNummer());
                    row.createCell(2).setCellValue(account.getOmschrijving());
                    // System.out.println(account.getNummer() + " " + account.getOmschrijving());
                }
                rowNum++;
            }
        }
    }

    private void populateUniqueSheet(Sheet sheet, List<LedgerAccount> uniqueLedgerAccounts) {
        int rowNum = 0;
        Customer currentCustomer = new Customer("x", "x"); // Initialize with non-existent customer
        LedgerAccount currentLedgerAccount;

        for (int i = 0; i < uniqueLedgerAccounts.size(); i++) {
            currentLedgerAccount = uniqueLedgerAccounts.get(i);
            
            // Set customer name atop their duplicate ledger accounts
            if (!currentCustomer.equals(currentLedgerAccount.getCustomers().get(0))) { // Unique ledger accounts only have one associated customer (index: 0)
                currentCustomer = currentLedgerAccount.getCustomers().get(0);
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(currentCustomer.getName());
            }

            // Write the duplicate ledger accounts
            Row row = sheet.createRow(rowNum++);
            row.createCell(1).setCellValue(currentLedgerAccount.getNummer());
            row.createCell(2).setCellValue(currentLedgerAccount.getOmschrijving());
        }
    }

    private void populateMismatchedSheet(Sheet sheet, Map<String, List<LedgerAccount>> mismatchedLedgerAccounts) {
        int rowNum = 0;
        for (Map.Entry<String, List<LedgerAccount>> entry : mismatchedLedgerAccounts.entrySet()) {
            String description = entry.getKey();
            List<LedgerAccount> accounts = entry.getValue();
    
            for (LedgerAccount account : accounts) {
                Row row = sheet.createRow(rowNum++);
                int cellCount = 0;
                row.createCell(cellCount++).setCellValue(description);
                row.createCell(cellCount++).setCellValue(account.getNummer());
                row.createCell(cellCount++).setCellValue(customersToString(account.getCustomers()));
                for (Customer customer : account.getCustomers()) {
                    row.createCell(cellCount++).setCellValue(customer.getName());
                }
            }
            rowNum++;
        }
    }

    private void populateUniformSheet(Sheet sheet, List<LedgerAccount> uniformLedgerAccounts) {
        int rowNum = 0;

        // Create header row
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Nummer");
        row.createCell(1).setCellValue("Omschrijving");
        row.createCell(2).setCellValue("Uniformiteit percentage");
        row.createCell(3).setCellValue("Klanten met grootboek");

        // Populate rows with ledger account information
        for (LedgerAccount ledgerAccount : uniformLedgerAccounts) {
            row = sheet.createRow(rowNum++);
            int cellCount = 0;
            row.createCell(cellCount++).setCellValue(ledgerAccount.getNummer());
            row.createCell(cellCount++).setCellValue(ledgerAccount.getOmschrijving());
            row.createCell(cellCount++).setCellValue(ledgerAccount.getUniformityPercentage());
            if (ledgerAccount.getCustomers() != null) {
                row.createCell(cellCount++).setCellValue(customersToString(ledgerAccount.getCustomers()));
                for (Customer customer : ledgerAccount.getCustomers()) {
                    row.createCell(cellCount++).setCellValue(customer.getName());
                }
            }
        }
    }

    // Helper method to convert customer list to string
    private String customersToString(List<Customer> customers) {
        StringBuilder sb = new StringBuilder();
        for (Customer customer : customers) {
            sb.append(customer.toString()).append("; ");
        }
        return sb.toString();
    }
}
