import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper class to format dates as expected on the <b>diff.json</b> file.
 */
public final class DateFormatter {

    /**
     * <b>IMPORTANT:</b> the requirement says that it has to be in CEST (Oslo - UTC+2), 
     *  however for the sake of this test I'm deciding to keep if fixed to UTC+2 (regardless of daylight savings)
     * @param date
     * @return
     */
    static final String formatDateFromString(String date){
        Instant instant = Instant.parse(date);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC+2"));
        
        // ZonedDateTime dateWithTimeZone = ZonedDateTime.of(ldt, ZoneId.of("Europe/Oslo")); // at the time of this test, Oslo is UTC+1
        ZonedDateTime dateWithTimeZone = ZonedDateTime.of(ldt, ZoneId.of("UTC+2"));
        String withTimeZone = dateWithTimeZone.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return withTimeZone.substring(0, 22);
    }
}