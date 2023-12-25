package dev.shingi;

import dev.shingi.API.Endpoint;
import dev.shingi.API.endpoints.models.ModelEndpoint;
import dev.shingi.API.endpoints.models.OData.Grootboek;
import dev.shingi.models.*;
import dev.shingi.services.*;
import dev.shingi.utils.*;

import java.io.*;

public class Main {

    private static CustomerList customerList;
    
    public static void main(String[] args) throws IOException {
        /*
         * Uncomment the two lines below if reading customers through Snelstart API
         */
        CustomerReaderSnelstart snelstartCustomerReader = new CustomerReaderSnelstart(); 
        customerList = snelstartCustomerReader.readCustomerData(true);

        Endpoint grootboeken = new ModelEndpoint<Grootboek>("grootboeken", Grootboek.class);

        /*
         * Uncomment the two lines below if reading customers from Excel file
         */
        // CustomerReaderExcel customerReaderExcel = new CustomerReaderExcel();
        // customerList = customerReaderExcel.readCustomerData(true);

        ExcelExporter excelExporter = new ExcelExporter();
        excelExporter.exportComparisonsToExcel(
            customerList.getDuplicateLedgerAccounts(), 
            customerList.getUniqueLedgerAccounts(), 
            customerList.getMismatchedLedgerAccounts(),
            customerList.getUniformLedgerAccounts(), 
            FILE_OUTPUT_PATH + "\\Comparison results.xlsx");
    }
}
    public static boolean saveData() {
		try {
			ResourceManager.saveData(Main.save, "data");
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}
}

