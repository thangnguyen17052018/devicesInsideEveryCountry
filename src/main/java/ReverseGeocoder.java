import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ReverseGeocoder {
    private static final String GEOCODING_RESOURCE = "https://revgeocode.search.hereapi.com/v1/revgeocode";
    private static final String API_KEY = "au0q1sA6wl3opHfXty_Vr8OC0NY9qiYuqLANW_Tj3Iw";

    public CompletableFuture<HttpResponse<String>> reverseGeocodeAt(String query) {
        HttpClient httpClient = HttpClient.newHttpClient();

        String encodeQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String requestUri = GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&at=" + encodeQuery + "&lang=en-US";

        HttpRequest revGeocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000))
                .build();

        return httpClient.sendAsync(revGeocodingRequest, HttpResponse.BodyHandlers.ofString());
    }

}
