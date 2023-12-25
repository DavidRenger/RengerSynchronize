package dev.shingi.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dev.shingi.API.endpoints.models.OData.Relatie;

public class ExcelUtils {

    private static String fileOutputPath = "C:\\Users\\damar\\OneDrive\\Documents\\Programmeren\\Visual Studio Code";
    

    public static void writeRelatieMemosToExcel(List<Relatie> relaties) {
        Workbook workbook = new XSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);

        for (Relatie relatie : relaties) {
            if (workbook.getSheetIndex(relatie.getNaam()) < 0) {
                // If sheet does not yet exist for the relationship
                if (relatie.getMemo() != null) {
                    if (!relatie.getMemo().isEmpty()) {
                        // If relatie has a memo
                        Sheet sheet = workbook.createSheet(relatie.getNaam());
                        sheet.setColumnWidth(0, 50 * 256); // The width is in units of 1/256th of a character width
                        Cell cell = sheet.createRow(0).createCell(0);
                        cell.setCellValue(relatie.getMemo());
                        cell.setCellStyle(cellStyle);
                        System.out.println("Sheet created for " + relatie.getNaam() + ", and memo entered.");
                    }
                }
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(ExcelUtils.fileOutputPath + "\\Relatie memos.xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
