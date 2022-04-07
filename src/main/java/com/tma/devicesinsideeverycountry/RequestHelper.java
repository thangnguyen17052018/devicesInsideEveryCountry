package com.tma.devicesinsideeverycountry;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.tma.devicesinsideeverycountry.Constant.*;

public class RequestHelper {

    private final CountryExaminer countryExaminer;

    public RequestHelper(CountryExaminer countryExaminer) {
        this.countryExaminer = countryExaminer;
    }

    public String getRequestIdInXMLResponse(CoordinateReader coordinateReader) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        String xmlText = countryExaminer.getBulkPostResponse(coordinateReader.createRequestBodyFromExcelFile());

        return XMLParser.getInstance().parseXML(xmlText, REQUEST_ID_TAG);
    }

    public String getJobStatusInString(String requestID) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        String xmlStatusText = countryExaminer.getJobStatus(requestID);

        return XMLParser.getInstance().parseXML(xmlStatusText, JOB_STATUS_TAG);
    }

    public boolean decideToProcessingGetResult(String jobStatus, String requestID) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        int attempt = 1;

        while (!COMPLETED_STATUS.equals(jobStatus)) {
            jobStatus = getJobStatusInString(requestID);

            if (checkStatusNotCompleted(jobStatus) || attempt > MAX_ATTEMP) {
                return false;
            }

            attempt++;
        }

        return true;
    }

    private boolean checkStatusNotCompleted(String jobStatus) {
        return DELETED_STATUS.equals(jobStatus) || CANCELLED_STATUS.equals(jobStatus) || FAILED_STATUS.equals(jobStatus);
    }



}
