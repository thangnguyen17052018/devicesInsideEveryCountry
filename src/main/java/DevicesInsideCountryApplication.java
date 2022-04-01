import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevicesInsideCountryApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        CountryExaminer countryExaminer = new CountryExaminer();
        CoordinateReader reader = new CoordinateReader();
        List<Coordinate> listCoordinates = reader.getCoordinates();

        String xmlText = countryExaminer.getPostBatchResponse(listCoordinates);
        XMLParser xmlParser = XMLParser.getInstance();
        String requestID = xmlParser.parseXML(xmlText, Constant.REQUEST_ID_TAG);

        String xmlStatusText = countryExaminer.getJobStatus(requestID);
        String status = xmlParser.parseXML(xmlStatusText, Constant.JOB_STATUS_TAG);
        boolean isComplete = true;
        while (!Constant.COMPLETED_STATUS.equals(status)) {

            xmlStatusText = countryExaminer.getJobStatus(requestID);
            status = xmlParser.parseXML(xmlStatusText, Constant.JOB_STATUS_TAG);

            if (Constant.DELETED_STATUS.equals(status) || Constant.CANCELLED_STATUS.equals(status)) {
                isComplete = false;
                break;
            }
        }

        if (isComplete) {
            String result = countryExaminer.getResponse(requestID);
            String[] tokenLines = result.split("\n");
            Map<String, Integer> devicesEveryCountryMap = new HashMap<>();

            for (String tokenLine : tokenLines) {
                String[] record = tokenLine.split("\\|");
                String seqNumber = record[1];
                if ("1".equals(seqNumber)) {
                    String countryName = record[3];
                    int count = devicesEveryCountryMap.getOrDefault(countryName, 0);
                    devicesEveryCountryMap.put(countryName, count + 1);
                }
            }

            System.out.println("All " + reader.getNumberOfDevices() + " devices");

            devicesEveryCountryMap.forEach((countryName, numberOfDevices) -> System.out.println(countryName + ": " + numberOfDevices + " devices"));
        } else {
            System.out.println("Failed to running batch jobs");
        }

    }

}
