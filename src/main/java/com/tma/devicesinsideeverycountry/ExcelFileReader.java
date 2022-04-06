package com.tma.devicesinsideeverycountry;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class ExcelFileReader {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelFileReader(String filePath) throws IOException {
        File source = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(source);
        workbook = new XSSFWorkbook(Objects.requireNonNull(fileInputStream));
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
