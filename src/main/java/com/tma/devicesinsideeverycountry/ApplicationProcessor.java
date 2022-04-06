package com.tma.devicesinsideeverycountry;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.tma.devicesinsideeverycountry.Constant.*;

@NoArgsConstructor
public class ApplicationProcessor {

    private static ApplicationProcessor applicationProcessor;

    public static ApplicationProcessor getInstance() {
        if (applicationProcessor == null) {
            applicationProcessor = new ApplicationProcessor();
        }
        return applicationProcessor;
    }

    public Map<String, Long> processIdentifyDevicesEveryCountry(String resultResponse) {
        String[] recordLines = resultResponse.split(BREAK_LINE_REGEX);
        Map<String, Long> devicesEveryCountryMap = new HashMap<>();

        for (String recordLine : recordLines) {
            String[] record = recordLine.split(OUTPUT_FIELD_DELIMITER);
            String seqNumber = record[SEQ_NUMBER_INDEX];

            Function<String, Boolean> getOneResultAmongAmbiguousResults = "1"::equals;

            if (getOneResultAmongAmbiguousResults.apply(seqNumber)) {
                String countryName = record[COUNTRY_NAME_INDEX];
                Long count = devicesEveryCountryMap.getOrDefault(countryName, 0L);
                devicesEveryCountryMap.put(countryName, ++count);
            }
        }

        return devicesEveryCountryMap;
    }

}
