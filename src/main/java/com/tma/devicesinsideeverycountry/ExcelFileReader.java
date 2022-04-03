package com.tma.devicesinsideeverycountry;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelFileReader {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelFileReader(String filePath) {
        try {
            File source = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(source);
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public double getDataNumeric(int sheetIndex, int row, int column) {
        sheet = workbook.getSheetAt(sheetIndex);
        return sheet.getRow(row).getCell(column).getNumericCellValue();
    }

    public String getDataString(int sheetIndex, int row, int column) {
        sheet = workbook.getSheetAt(sheetIndex);
        return sheet.getRow(row).getCell(column).getStringCellValue();
    }

    public int getRowCount(int sheetIndex) {
        int row = workbook.getSheetAt(sheetIndex).getLastRowNum();
        return ++row;
    }

}
