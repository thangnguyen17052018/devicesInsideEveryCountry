import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcelFile {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ReadExcelFile(String filePath) {
        try {
            File source = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(source);
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public double getData(int sheetIndex, int row, int column) {
        sheet = workbook.getSheetAt(sheetIndex);
        return sheet.getRow(row).getCell(column).getNumericCellValue();
    }

    public int getRowCount(int sheetIndex) {
        int row = workbook.getSheetAt(sheetIndex).getLastRowNum();
        return ++row;
    }

}
