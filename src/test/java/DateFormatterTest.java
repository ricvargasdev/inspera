import static org.junit.Assert.*;
import org.junit.Test;

public class DateFormatterTest {

    @Test
    public void testParser(){
        assertEquals("Test that the formatter returns the expected date according to the requirements from the readme.md file", "2016-01-20T18:00:00+02", DateFormatter.formatDateFromString("2016-01-20T16:00:00Z"));
    }
}