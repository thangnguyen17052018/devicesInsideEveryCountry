package com.tma.devicesinsideeverycountry;

import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class CountryExaminer {

    private final ReverseGeocoder reverseGeocoder = new ReverseGeocoder();

    public String getBulkPostResponse(String requestBody) throws IOException, InterruptedException {
        return reverseGeocoder.sendPostBatchRequestReverseGeocoding(requestBody).body();
    }

    public String getJobStatus(String requestId) throws IOException, InterruptedException {
        return reverseGeocoder.sendGetRequestJobStatus(requestId).body();
    }

    public String getResult(String requestId) throws IOException, InterruptedException {
        return reverseGeocoder.getResponseResult(requestId).body();
    }

}
