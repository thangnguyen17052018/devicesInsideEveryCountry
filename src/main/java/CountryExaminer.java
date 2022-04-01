import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class CountryExaminer {
    private final ReverseGeocoder reverseGeocoder = new ReverseGeocoder();

    public String getPostBatchResponse(List<Coordinate> coordinateList) throws IOException, InterruptedException {
        return reverseGeocoder.postBatchRequestReverseGeocoding(coordinateList).body();
    }

    public String getJobStatus(String requestId) throws IOException, InterruptedException {
        return reverseGeocoder.getRequestJobStatus(requestId).body();
    }

    public String getResponse(String requestId) throws IOException, InterruptedException {
        return reverseGeocoder.getResponse(requestId).body();
    }
}
