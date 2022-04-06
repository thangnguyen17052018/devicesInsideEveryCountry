import com.tma.devicesinsideeverycountry.Constant;
import com.tma.devicesinsideeverycountry.CoordinateReader;
import com.tma.devicesinsideeverycountry.CountryExaminer;
import com.tma.devicesinsideeverycountry.RequestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RequestHelperTest {

    private CoordinateReader coordinateReader;

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        coordinateReader = new CoordinateReader();
        requestHelper = new RequestHelper(new CountryExaminer());
    }

    @Mock
    CountryExaminer countryExaminer;

    @InjectMocks
    private RequestHelper requestHelper;


    @Test
    public void testGetRequestIdInXMLResponse() throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        String requestId = requestHelper.getRequestIdInXMLResponse(coordinateReader);
        assertThat(requestId).isNotNull().isNotEmpty();
    }

    @Test
    public void testDecideNotToProcessingCausedByCancelStatus() throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        boolean isStatusCompleted = requestHelper.decideToProcessingGetResult(Constant.CANCELLED_STATUS, "39931afasf");
        assertThat(isStatusCompleted).isFalse();
    }

    @Test
    public void testDecideToProcessingGetResult() throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        boolean isStatusCompleted = requestHelper.decideToProcessingGetResult(Constant.COMPLETED_STATUS, "39931afasf");
        assertThat(isStatusCompleted).isTrue();
    }

    @Test
    public void testDecideNotToProcessingByOutOfLimitAttempts() throws IOException, ParserConfigurationException, InterruptedException, SAXException {
        requestHelper.getRequestIdInXMLResponse(coordinateReader);
        boolean isStatusCompleted = requestHelper.decideToProcessingGetResult(Constant.ACCEPT_STATUS, "39931afasf");
        Mockito.when(countryExaminer.getJobStatus("39931afasf")).thenReturn(Constant.ACCEPT_STATUS);
        assertThat(isStatusCompleted).isFalse();
    }

}
