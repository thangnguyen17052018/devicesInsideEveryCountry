import com.tma.devicesinsideeverycountry.ApplicationProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationProcessorTest {

    private ApplicationProcessor applicationProcessor;

    @Before
    public void setUp() {
        applicationProcessor = ApplicationProcessor.getInstance();
    }

    @Test
    public void testProcessIdentifyDevicesEveryCountryReturnMap() {
        String resultResponse = """
                1|1|2|France
                1|2|2|France
                2|1|7|Italy
                2|2|7|Italy
                2|3|7|Italy
                2|4|7|Italy
                2|5|7|Italy
                2|6|7|Italy
                2|7|7|Italy
                """;
        Map<String, Long> devicesEveryCountryMap = applicationProcessor.processIdentifyDevicesEveryCountry(resultResponse);
        assertThat(devicesEveryCountryMap.size()).isGreaterThan(0);
    }

    @Test
    public void testProcessIdentifyDevicesEveryCountryReturnRightResultExcludeAmbiguousResults() {
        String resultResponse = """
                1|1|2|France
                1|2|2|France
                2|1|7|Italy
                2|2|7|Italy
                2|3|7|Italy
                2|4|7|Italy
                2|5|7|Italy
                2|6|7|Italy
                2|7|7|Italy
                """;
        Map<String, Long> devicesEveryCountryMap = applicationProcessor.processIdentifyDevicesEveryCountry(resultResponse);
        assertThat(devicesEveryCountryMap.size()).isGreaterThan(0).isEqualTo(2);
    }


}
