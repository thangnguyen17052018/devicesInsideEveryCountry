import com.tma.devicesinsideeverycountry.XMLParser;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.tma.devicesinsideeverycountry.Constant.JOB_STATUS_TAG;
import static com.tma.devicesinsideeverycountry.Constant.REQUEST_ID_TAG;

public class XMLParserTest {

    public static final String XML_TEST = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:SearchBatch xmlns:ns2=\"https://www.navteq.com/lbsp/Search-Batch/1\"><Response><MetaInfo><RequestId>YZoQZIAzFtyETWNPtjVmd0JE3zifAc0N</RequestId></MetaInfo><Status>accepted</Status><TotalCount>0</TotalCount><ValidCount>0</ValidCount><InvalidCount>0</InvalidCount><ProcessedCount>0</ProcessedCount><PendingCount>0</PendingCount><SuccessCount>0</SuccessCount><ErrorCount>0</ErrorCount></Response></ns2:SearchBatch>\n";
    private XMLParser xmlParser;

    @Before
    public void setUp() {
        xmlParser = XMLParser.getInstance();
    }

    @Test(expected = SAXParseException.class)
    public void testParseXMLSAXParseMarkupNotWellFormed() throws ParserConfigurationException, IOException, SAXException {
        String xmlTest = "<Status>accepted</Status><TotalCount>0</TotalCount><ValidCount>0</ValidCount><InvalidCount>0</InvalidCount>";
        xmlParser.parseXML(xmlTest, JOB_STATUS_TAG);
    }

    @Test
    public void testParseCorrectStatusTag() throws ParserConfigurationException, IOException, SAXException {
        String result = xmlParser.parseXML(XML_TEST, JOB_STATUS_TAG);

        Assertions.assertThat(result).isNotEmpty().isEqualTo("accepted");
    }

    @Test
    public void testParseCorrectRequestIDTag() throws ParserConfigurationException, IOException, SAXException {
        String result = xmlParser.parseXML(XML_TEST, REQUEST_ID_TAG);

        Assertions.assertThat(result).isNotNull().isNotEmpty();
    }

}
