import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class CountryExaminer {
    private final ReverseGeocoder reverseGeocoder = new ReverseGeocoder();

    public String getBulkPostResponse(List<Coordinate> coordinateList) throws IOException, InterruptedException {
        return reverseGeocoder.sendPostBatchRequestReverseGeocoding(coordinateList).body();
    }

    public String getJobStatus(String requestId) throws IOException, InterruptedException {
        return reverseGeocoder.sendGetRequestJobStatus(requestId).body();
    }

    public String getResult(String requestId) throws IOException, InterruptedException {
        return reverseGeocoder.getResponseResult(requestId).body();
    }
}
