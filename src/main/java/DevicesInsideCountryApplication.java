import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicesInsideCountryApplication {

    public static void main(String[] args) {

        CoordinateReader reader = new CoordinateReader();
        List<Coordinate> listCoordinates = reader.getCoordinates();

        Map<String, Integer> devicesEveryCountryMap = new HashMap<>();
        CountryExaminer countryExaminer = new CountryExaminer();

        listCoordinates.forEach(coordinate -> {
            try {
                if (!coordinate.isNullIsLand()) {
                    String countryName = countryExaminer.examineCountryNameOf(coordinate);
                    int count = devicesEveryCountryMap.getOrDefault(countryName, 0);
                    devicesEveryCountryMap.put(countryName, count + 1);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("All " + reader.getNumberOfDevices() + " devices");

        devicesEveryCountryMap.forEach((countryName, numberOfDevices) -> System.out.println(countryName + ": " + numberOfDevices + " devices"));

    }

}
