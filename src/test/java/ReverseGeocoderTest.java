import com.tma.devicesinsideeverycountry.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.http.HttpResponse;

import static com.tma.devicesinsideeverycountry.Constant.JOB_STATUS_TAG;
import static com.tma.devicesinsideeverycountry.Constant.REQUEST_ID_TAG;
import static org.assertj.core.api.Assertions.assertThat;

public class ReverseGeocoderTest {

    private ReverseGeocoder reverseGeocoder;
    private CoordinateReader coordinateReader;
    private RequestHelper requestHelper;

    @Before
    public void init() throws IOException {
        reverseGeocoder = new ReverseGeocoder();
        ExcelFileReader excelFileReader = new ExcelFileReader(Constant.EXCEL_FILE_PATH);
        coordinateReader = new CoordinateReader(excelFileReader);
        requestHelper = new RequestHelper(new CountryExaminer());
    }

    @Test
    public void testPostPostBatchRequestReverseGeocodingStatusOK() throws IOException, InterruptedException {
        String requestBody = coordinateReader.createRequestBodyFromExcelFile();
        HttpResponse<String> response = reverseGeocoder.sendPostBatchRequestReverseGeocoding(requestBody);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    public void testSendGetRequestJobStatusOK() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        String requestBody = coordinateReader.createRequestBodyFromExcelFile();
        HttpResponse<String> revGeocodingResponse = reverseGeocoder.sendPostBatchRequestReverseGeocoding(requestBody);
        XMLParser xmlParser = XMLParser.getInstance();
        String requestID = xmlParser.parseXML(revGeocodingResponse.body(), REQUEST_ID_TAG);

        HttpResponse<String> response = reverseGeocoder.sendGetRequestJobStatus(requestID);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    public void sendGetResponseResultStatusOK() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        String requestBody = coordinateReader.createRequestBodyFromExcelFile();
        HttpResponse<String> revGeocodingResponse = reverseGeocoder.sendPostBatchRequestReverseGeocoding(requestBody);
        XMLParser xmlParser = XMLParser.getInstance();
        String requestID = xmlParser.parseXML(revGeocodingResponse.body(), REQUEST_ID_TAG);

        String jobStatus = xmlParser.parseXML(reverseGeocoder.sendGetRequestJobStatus(requestID).body(), JOB_STATUS_TAG);

        boolean statusIsCompleted = requestHelper.decideToProcessingGetResult(jobStatus, requestID);
        if (statusIsCompleted) {
            HttpResponse<String> response = reverseGeocoder.getResponseResult(requestID);

            assertThat(response.statusCode()).isEqualTo(200);
        }
    }

    @Test
    public void sendGetResponseStatus404WhenBatchJobNotCompleted() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        String requestBody = coordinateReader.createRequestBodyFromExcelFile();
        HttpResponse<String> revGeocodingResponse = reverseGeocoder.sendPostBatchRequestReverseGeocoding(requestBody);
        XMLParser xmlParser = XMLParser.getInstance();
        String requestID = xmlParser.parseXML(revGeocodingResponse.body(), REQUEST_ID_TAG);

        HttpResponse<String> response = reverseGeocoder.getResponseResult(requestID);
        assertThat(response.statusCode()).isEqualTo(404);
    }

}
