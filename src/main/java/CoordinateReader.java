import java.util.ArrayList;
import java.util.List;

public class CoordinateReader {
    private final ExcelFileReader reader = new ExcelFileReader("F:\\location data_2022_02_10.xlsx");
    public static final int LATITUDE_COLUMN = 8;
    public static final int LONGITUDE_COLUMN = 9;
    public static final int SHEET_DATA_INDEX = 1;
    private final List<Coordinate> listCoordinates = new ArrayList<>();

    public List<Coordinate> getCoordinates() {
        int rows = reader.getRowCount(SHEET_DATA_INDEX);

        for (int i = 1; i < rows; i++) {
            double latitude = reader.getData(1, i, LATITUDE_COLUMN);
            double longitude = reader.getData(1, i, LONGITUDE_COLUMN);
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
