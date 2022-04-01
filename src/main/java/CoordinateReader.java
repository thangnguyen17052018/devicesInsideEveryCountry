import java.util.ArrayList;
import java.util.List;

public class CoordinateReader {
    private final ReadExcelFile reader = new ReadExcelFile("F:\\location data_2022_02_10.xlsx");
    public static final int LATITUDE_COLUMN = 8;
    public static final int LONGITUDE_COLUMN = 9;
    public static final int SHEET_DATA_INDEX = 1;
    private final List<Coordinate> listCoordinates = new ArrayList<>();

    public List<Coordinate> getCoordinates() {
        int rows = reader.getRowCount(SHEET_DATA_INDEX);

        for (int i = 1; i < rows; i++) {
            double latitude = reader.getData(1, i, LATITUDE_COLUMN);
            double longitude = reader.getData(1, i, LONGITUDE_COLUMN);

            listCoordinates.add(new Coordinate(latitude, longitude));
        }

        return listCoordinates;
    }

    public void printCoordinates(List<Coordinate> listCoordinates) {
        listCoordinates.forEach(System.out::println);
    }

    public int getNumberOfDevices() {
        return reader.getRowCount(SHEET_DATA_INDEX) - 1;
    }

}
