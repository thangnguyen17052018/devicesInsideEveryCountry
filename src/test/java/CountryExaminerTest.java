import com.tma.devicesinsideeverycountry.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.tma.devicesinsideeverycountry.Constant.JOB_STATUS_TAG;
import static com.tma.devicesinsideeverycountry.Constant.REQUEST_ID_TAG;
import static org.assertj.core.api.Assertions.assertThat;

public class CountryExaminerTest {

    private CountryExaminer examiner;
    private CoordinateReader coordinateReader;
    private RequestHelper helper;

    @Before
    public void setUp() throws IOException {
        examiner = new CountryExaminer();
        ExcelFileReader excelFileReader = new ExcelFileReader(Constant.EXCEL_FILE_PATH);
        coordinateReader = new CoordinateReader(excelFileReader);
        helper = new RequestHelper(examiner);
    }

    @Test
    public void testGetBulkPostResponseSuccessful() throws IOException, InterruptedException {
        String requestBody = coordinateReader.createRequestBodyFromExcelFile();
        String result = examiner.getBulkPostResponse(requestBody);

        assertThat(result).isNotNull().isNotEmpty();
    }

    @Test
    public void testGetJobStatusSuccessful() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        String requestBody = coordinateReader.createRequestBodyFromExcelFile();
        String bulkPostResponse = examiner.getBulkPostResponse(requestBody);
        XMLParser xmlParser = XMLParser.getInstance();

        String requestID = xmlParser.parseXML(bulkPostResponse, REQUEST_ID_TAG);

        String result = examiner.getJobStatus(requestID);

        assertThat(result).isNotNull().isNotEmpty();
    }

    @Test
    public void testGetResultSuccessful() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        String requestBody = coordinateReader.createRequestBodyFromExcelFile();
        String bulkPostResponse = examiner.getBulkPostResponse(requestBody);
        XMLParser xmlParser = XMLParser.getInstance();

        String requestID = xmlParser.parseXML(bulkPostResponse, REQUEST_ID_TAG);
        String xmlStatusText = examiner.getJobStatus(requestID);
        String jobStatus = xmlParser.parseXML(xmlStatusText, JOB_STATUS_TAG);

        boolean statusIsCompleted = helper.decideToProcessingGetResult(jobStatus, requestID);

        if (statusIsCompleted) {
            String result = examiner.getResult(requestID);
            assertThat(result).isNotNull().isNotEmpty();
        }
    }
}