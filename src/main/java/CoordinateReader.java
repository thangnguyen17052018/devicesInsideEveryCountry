import java.util.ArrayList;
import java.util.List;

public class CoordinateReader {
    private final ExcelFileReader reader = new ExcelFileReader(Constant.EXCEL_FILE_PATH);
    public static final int LATITUDE_COLUMN = 8;
    public static final int LONGITUDE_COLUMN = 9;
    public static final int SHEET_DATA_INDEX = 1;
    public static final int REPORT_DATA_INDEX = 1;

    private final List<Coordinate> listCoordinates = new ArrayList<>();

    public List<Coordinate> getCoordinates() {
        int rows = reader.getRowCount(SHEET_DATA_INDEX);

        for (int i = 1; i < rows; i++) {
            double latitude = reader.getData(REPORT_DATA_INDEX, i, LATITUDE_COLUMN);
            double longitude = reader.getData(REPORT_DATA_INDEX, i, LONGITUDE_COLUMN);
            Coordinate coordinate = new Coordinate(latitude, longitude);
            if (!coordinate.isNullIsLand()) {
                listCoordinates.add(coordinate);
            }
        }

        return listCoordinates;
    }

    public int getNumberOfDevices() {
        return reader.getRowCount(SHEET_DATA_INDEX) - 1;
    }

}
