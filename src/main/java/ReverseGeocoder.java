import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class ReverseGeocoder {
    private static final String BATCH_GEOCODING_RESOURCE = "https://batch.geocoder.ls.hereapi.com/6.2/jobs";
    private static final String API_KEY = "au0q1sA6wl3opHfXty_Vr8OC0NY9qiYuqLANW_Tj3Iw";

    public HttpResponse<String> sendPostBatchRequestReverseGeocoding(List<Coordinate> coordinateList) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        String bodyString = createMessageBody(coordinateList);
        String requestUri = BATCH_GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&gen=8&action=run&header=false&indelim=%7C&outdelim=%7C&outcols=addressDetailsCountry&locationattributes=addressDetails&outputCombined=true&mode=retrieveAddresses&responseattributes=matchCode,parsedRequest&language=en";

        HttpRequest batchRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(bodyString, StandardCharsets.UTF_8))
                .uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(10000))
                .build();

        return httpClient.send(batchRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> sendGetRequestJobStatus(String requestId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String requestUri = BATCH_GEOCODING_RESOURCE + "/" + requestId + "?action=status&apiKey=" + API_KEY;

        HttpRequest revGeocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(10000))
                .build();

        return httpClient.send(revGeocodingRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getResponseResult(String requestId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String requestUri = BATCH_GEOCODING_RESOURCE + "/" + requestId + "/result?apiKey=" + API_KEY + "&outputcompressed=false&matchCode=exact&parsedRequest=parsedRequestCountry";

        HttpRequest revGeocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(10000))
                .build();

        return httpClient.send(revGeocodingRequest, HttpResponse.BodyHandlers.ofString());
    }

    private String createMessageBody(List<Coordinate> coordinateList) {
        StringBuilder bodyString = new StringBuilder("recId|prox\n");
        for (int i = 0; i < coordinateList.size(); i++) {
            bodyString.append(String.format("%d|%s,%s,0\n", i + 1, coordinateList.get(i).getLatitude(), coordinateList.get(i).getLongitude()));
        }
        return bodyString.toString();
    }

}
