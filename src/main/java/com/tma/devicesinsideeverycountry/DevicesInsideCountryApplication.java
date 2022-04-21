package com.tma.devicesinsideeverycountry;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

public class DevicesInsideCountryApplication {

    public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        CountryExaminer countryExaminer = new CountryExaminer();
        ExcelFileReader excelFileReader = new ExcelFileReader(Constant.EXCEL_FILE_PATH);
        CoordinateReader reader = new CoordinateReader(excelFileReader);
        RequestHelper helper = new RequestHelper(countryExaminer);

        String requestID = helper.getRequestIdInXMLResponse(reader);
        String jobStatus = helper.getJobStatusInString(requestID);

        boolean statusIsCompleted = helper.decideToProcessingGetResult(jobStatus, requestID);

        if (statusIsCompleted) {
            String result = countryExaminer.getResult(requestID);
            Map<String, Long> devicesEveryCountryMap = ApplicationProcessor.getInstance().processIdentifyDevicesEveryCountry(result);
            Long countDevicesInUsed = devicesEveryCountryMap.values().stream().reduce(0L, Long::sum);
            System.out.println("All " + reader.getNumberOfDevices() + " devices which " + countDevicesInUsed + " devices in used");

            devicesEveryCountryMap.forEach((countryName, numberOfDevices) -> System.out.println(countryName + ": " + numberOfDevices + ((numberOfDevices > 1) ? " devices" : " device")));
        } else {
            System.out.println("Failed to running batch jobs");
        }

    }

}
