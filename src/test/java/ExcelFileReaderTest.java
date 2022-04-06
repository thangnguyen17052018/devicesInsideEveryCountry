import com.tma.devicesinsideeverycountry.Constant;
import com.tma.devicesinsideeverycountry.ExcelFileReader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelFileReaderTest {

    public static final String REPORT_DATA_SHEET_NAME = "Report Data";
    public static final int NUMBER_OF_ROWS_IN_SHEET_DATA = 3119;
    public static final String WRONG_FILE_PATH = "D:\\locationWRONG_2022_02_10.xlsx";
    private ExcelFileReader reader;

    @Before
    public void setUp() throws IOException {
        reader = new ExcelFileReader(Constant.EXCEL_FILE_PATH);
    }

    @Test
    public void testFileInstanceExist() {
        File source = new File(Constant.EXCEL_FILE_PATH);
        assertThat(source)
                .exists()
                .isFile()
                .canRead()
                .canWrite();
    }

    @Test
    public void testInputHasStreamContent() throws FileNotFoundException {
        File source = new File(Constant.EXCEL_FILE_PATH);
        FileInputStream fileInputStream = new FileInputStream(source);
        assertThat(fileInputStream).isNotNull();
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundWhenWrongPath() throws FileNotFoundException {
        File source = new File(WRONG_FILE_PATH);
        new FileInputStream(source);
    }

    @Test
    public void testWorkbookIsNotNull() throws IOException {
        File source = new File(Constant.EXCEL_FILE_PATH);
        FileInputStream fileInputStream = new FileInputStream(source);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        assertThat(workbook.getSheetName(1)).isEqualTo(REPORT_DATA_SHEET_NAME);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCannotFoundFile() throws FileNotFoundException {
        new FileInputStream("null");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataNumericFailedWhenGetANumericValueFromAStringCell() {
        reader.getDataNumeric(Constant.REPORT_DATA_INDEX, 1, 1);
    }

    @Test
    public void testGetDataNumericLikeLatitude() {
        double data = reader.getDataNumeric(Constant.REPORT_DATA_INDEX, 1, Constant.LATITUDE_COLUMN);
        assertThat(data).isNotNaN();
        assertThat(data).isEqualTo(50.04826);
    }

    @Test
    public void testGetDataNumericLikeLongitude() {
        double data = reader.getDataNumeric(Constant.REPORT_DATA_INDEX, 1, Constant.LONGITUDE_COLUMN);
        assertThat(data).isNotNaN();
        assertThat(data).isEqualTo(2.93393);
    }

    @Test
    public void testGetDataStringLikeHeader() {
        String data = reader.getDataString(Constant.REPORT_DATA_INDEX, 0, Constant.LONGITUDE_COLUMN);
        assertThat(data).isNotEmpty();
        assertThat(data).isEqualTo("Current longitude");
    }

    @Test
    public void testGetDataStatusONLINE() {
        String data = reader.getDataString(Constant.REPORT_DATA_INDEX, 1, Constant.STATUS_COLUMN);
        assertThat(data).isNotEmpty();
        assertThat(data).isEqualTo("ONLINE");
    }

    @Test
    public void testGetDataStatusOFFLINE() {
        String data = reader.getDataString(Constant.REPORT_DATA_INDEX, 10, Constant.STATUS_COLUMN);
        assertThat(data).isNotEmpty();
        assertThat(data).isEqualTo("OFFLINE");
    }

    @Test
    public void testGetDataStatusREADY_FOR_USE() {
        String data = reader.getDataString(Constant.REPORT_DATA_INDEX, 235, Constant.STATUS_COLUMN);
        assertThat(data).isNotEmpty();
        assertThat(data).isEqualTo("READY_FOR_USE");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataStringFailedWhenGetAStringValueFromANumericCell() {
        reader.getDataString(Constant.REPORT_DATA_INDEX, 1, Constant.LONGITUDE_COLUMN);
    }

    @Test
    public void testGetRowCount() {
        int rowCount = reader.getRowCount(1);
        assertThat(rowCount).isNotNegative().isEqualTo(NUMBER_OF_ROWS_IN_SHEET_DATA);
    }
}
