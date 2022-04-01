import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DevicesInsideCountryApplication {

    public static final int SEQ_NUMBER_INDEX = 1;
    public static final int COUNTRY_NAME_INDEX = 3;

    public static void main(String[] args) throws IOException, InterruptedException {

        CountryExaminer countryExaminer = new CountryExaminer();
        CoordinateReader reader = new CoordinateReader();
        List<Coordinate> listCoordinates = reader.getCoordinates();

        String xmlText = countryExaminer.getBulkPostResponse(listCoordinates);

        XMLParser xmlParser = XMLParser.getInstance();

        String requestID = xmlParser.parseXML(xmlText, Constant.REQUEST_ID_TAG);

        String xmlStatusText = countryExaminer.getJobStatus(requestID);
        String status = xmlParser.parseXML(xmlStatusText, Constant.JOB_STATUS_TAG);

        boolean statusIsCompleted = true;

        while (!Constant.COMPLETED_STATUS.equals(status)) {
            xmlStatusText = countryExaminer.getJobStatus(requestID);
            status = xmlParser.parseXML(xmlStatusText, Constant.JOB_STATUS_TAG);

            if (Constant.DELETED_STATUS.equals(status) || Constant.CANCELLED_STATUS.equals(status)) {
                statusIsCompleted = false;
                break;
            }
        }

        if (statusIsCompleted) {
            String result = countryExaminer.getResult(requestID);
            String[] recordLines = result.split(Constant.BREAK_LINE_REGEX);
            Map<String, Integer> devicesEveryCountryMap = new HashMap<>();
            long countDevicesInUsed = 0L;

            for (String recordLine : recordLines) {
                String[] record = recordLine.split(Constant.OUTPUT_FIELD_DELIMITER);
                String seqNumber = record[SEQ_NUMBER_INDEX];

                Function<String, Boolean> getOneResultAmongAmbiguousResults = "1"::equals;
                if (getOneResultAmongAmbiguousResults.apply(seqNumber)) {
                    String countryName = record[COUNTRY_NAME_INDEX];
                    int count = devicesEveryCountryMap.getOrDefault(countryName, 0);
                    devicesEveryCountryMap.put(countryName, ++count);
                    countDevicesInUsed++;
                }
            }

            System.out.println("All " + reader.getNumberOfDevices() + " devices which " + countDevicesInUsed + " in used");

            devicesEveryCountryMap.forEach((countryName, numberOfDevices) -> System.out.println(countryName + ": " + numberOfDevices + " devices"));
        } else {
            System.out.println("Failed to running batch jobs");
        }

    }

}
