package com.tma.devicesinsideeverycountry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.tma.devicesinsideeverycountry.Constant.*;

public class DevicesInsideCountryApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        CountryExaminer countryExaminer = new CountryExaminer();
        CoordinateReader reader = new CoordinateReader();

        String xmlText = countryExaminer.getBulkPostResponse(reader.createRequestBodyFromExcelFile());

        XMLParser xmlParser = XMLParser.getInstance();

        String requestID = xmlParser.parseXML(xmlText, REQUEST_ID_TAG);

        String xmlStatusText = countryExaminer.getJobStatus(requestID);
        String status = xmlParser.parseXML(xmlStatusText, JOB_STATUS_TAG);

        boolean statusIsCompleted = true;

        while (!COMPLETED_STATUS.equals(status)) {
            xmlStatusText = countryExaminer.getJobStatus(requestID);
            status = xmlParser.parseXML(xmlStatusText, JOB_STATUS_TAG);

            if (DELETED_STATUS.equals(status) || CANCELLED_STATUS.equals(status)) {
                statusIsCompleted = false;
                break;
            }
        }

        if (statusIsCompleted) {
            String result = countryExaminer.getResult(requestID);
            String[] recordLines = result.split(BREAK_LINE_REGEX);
            Map<String, Integer> devicesEveryCountryMap = new HashMap<>();
            long countDevicesInUsed = 0L;

            for (String recordLine : recordLines) {
                String[] record = recordLine.split(OUTPUT_FIELD_DELIMITER);
                String seqNumber = record[SEQ_NUMBER_INDEX];

                Function<String, Boolean> getOneResultAmongAmbiguousResults = "1"::equals;
                if (getOneResultAmongAmbiguousResults.apply(seqNumber)) {
                    String countryName = record[COUNTRY_NAME_INDEX];
                    int count = devicesEveryCountryMap.getOrDefault(countryName, 0);
                    devicesEveryCountryMap.put(countryName, ++count);
                    ++countDevicesInUsed;
                }
            }

            System.out.println("All " + reader.getNumberOfDevices() + " devices which " + countDevicesInUsed + " devices in used");

            devicesEveryCountryMap.forEach((countryName, numberOfDevices) -> System.out.println(countryName + ": " + numberOfDevices + ((numberOfDevices > 1) ? " devices" : " device")));
        } else {
            System.out.println("Failed to running batch jobs");
        }

    }

}
