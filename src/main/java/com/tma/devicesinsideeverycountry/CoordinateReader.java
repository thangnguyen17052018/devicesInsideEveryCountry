package com.tma.devicesinsideeverycountry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.tma.devicesinsideeverycountry.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateReader {

    private ExcelFileReader reader;

    public String createRequestBodyFromExcelFile() {
        int rows = reader.getRowCount(REPORT_DATA_INDEX);
        StringBuilder bodyString = new StringBuilder("recId|prox\n");

        for (int i = 1; i < rows; i++) {
            double latitude = reader.getDataNumeric(REPORT_DATA_INDEX, i, LATITUDE_COLUMN);
            double longitude = reader.getDataNumeric(REPORT_DATA_INDEX, i, LONGITUDE_COLUMN);
            String status = reader.getDataString(REPORT_DATA_INDEX, i, STATUS_COLUMN);

            if (!READY_FOR_USE.equals(status)) {
                bodyString.append(String.format("%d|%s,%s,250\n", i, latitude, longitude));
            }
        }

        return bodyString.toString();
    }

    public int getNumberOfDevices() {
        return reader.getRowCount(SHEET_DATA_INDEX) - 1;
    }

}
