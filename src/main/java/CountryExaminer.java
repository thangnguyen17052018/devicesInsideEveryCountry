import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class CountryExaminer {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ReverseGeocoder reverseGeocoder = new ReverseGeocoder();

    public String examineCountryNameOf(Coordinate locationCoordinate) throws IOException, InterruptedException {
        String response = reverseGeocoder.reverseGeocodeAt(locationCoordinate.getLatitude() + "," + locationCoordinate.getLongitude())
                                            .join()
                                            .body();
        JsonNode responseJsonNode = mapper.readTree(response);
        String countryName = "";

        JsonNode items = responseJsonNode.get("items");
        for (JsonNode item : items) {
            JsonNode address = item.get("address");
            countryName = address.get("countryName").asText();
        }

        return countryName;
    }
}
