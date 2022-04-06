import com.tma.devicesinsideeverycountry.CoordinateReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CoordinateReadTest {

    CoordinateReader reader;

    @Before
    public void setUp() throws IOException {
        reader = new CoordinateReader();
    }

    @Test
    public void testGetNumberOfDevices() {
        assertThat(reader.getNumberOfDevices()).isEqualTo(3118);
    }

    @Test
    public void testCreateRequestBodyFromExcelFile() {
        assertThat(reader.createRequestBodyFromExcelFile()).isNotEmpty();
        assertThat(reader.createRequestBodyFromExcelFile().length()).isGreaterThan(0);
    }

}
