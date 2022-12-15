import java.io.InputStream;

import static org.junit.Assert.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;

import static net.javacrumbs.jsonunit.JsonMatchers.*;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;

public class ParserTest {

    Parser parser;
    JSONObject before, after, before2, after2;

    // Loads test data from before.json and after.json files
    @Before
    public void setup() {
        parser = new Parser();
        InputStream beforeInput, afterInput, beforeInput2, afterInput2;
        
        beforeInput = ParserTest.class.getResourceAsStream( "before.json");
        afterInput = ParserTest.class.getResourceAsStream( "after.json");
        before = new JSONObject(new JSONTokener(beforeInput));
        after = new JSONObject(new JSONTokener(afterInput));

        if(before == null) {
            System.out.println("Before input file not found...\n");
        }
        if(after == null) {
            System.out.println("After input file not found...\n");
        }

        beforeInput2 = ParserTest.class.getResourceAsStream( "before2.json");
        afterInput2 = ParserTest.class.getResourceAsStream( "after2.json");
        before2 = new JSONObject(new JSONTokener(beforeInput2));
        after2 = new JSONObject(new JSONTokener(afterInput2));

        if(before2 == null) {
            System.out.println("Before input file not found...\n");
        }
        if(after2 == null) {
            System.out.println("After input file not found...\n");
        }
    }

    @Test
    public void testFilesLoaded(){
        assertThat("Before file is loaded", before, jsonEquals("{\"candidates\":[{\"candidateName\":\"C1\",\"id\":10,\"extraTime\":0},{\"candidateName\":\"C2\",\"id\":11,\"extraTime\":10},{\"candidateName\":\"C3\",\"id\":12,\"extraTime\":20}],\"meta\":{\"startTime\":\"2016-01-20T10:00:00Z\",\"endTime\":\"2016-01-20T16:00:00Z\",\"title\":\"Title\"},\"id\":1}"));
        assertThat("After file is loaded", after, jsonEquals("{\"candidates\":[{\"candidateName\":\"C1\",\"id\":10,\"extraTime\":10},{\"candidateName\":\"C5\",\"id\":11,\"extraTime\":10},{\"candidateName\":\"C4\",\"id\":13,\"extraTime\":0}],\"meta\":{\"startTime\":\"2016-01-20T10:00:00Z\",\"endTime\":\"2016-01-20T18:00:00Z\",\"title\":\"New Title\"},\"id\":1}"));
    }

    @Test
    public void testParser(){
        JSONObject result = parser.parse(before, after);
        assertThat("Parser returns the expected output based on the requirements from readme.md", result, jsonEquals("{\"meta\": [{\"field\":\"title\",\"before\":\"Title\",\"after\":\"New Title\"},"+
            "{\"field\": \"endTime\",\"before\": \"2016-01-20T18:00:00+02\",\"after\": \"2016-01-20T20:00:00+02\"}]," + 
            "\"candidates\":{\"edited\":[{\"id\":10},{\"id\":11}],\"added\":[{\"id\":13}],\"removed\":[{\"id\":12}]}}" +
            "}"));
    }

    @Test
    public void testParserWithFullChanges(){
        JSONObject result = parser.parse(before2, after2);
        assertThat("Parser returns changes on all fields based on the requirements from readme.md", result, jsonEquals("{\"meta\": [{\"field\":\"title\",\"before\":\"Title\",\"after\":\"New Title\"},"+
            "{\"field\":\"startTime\",\"before\":\"2016-01-20T12:00:00+02\",\"after\":\"2016-01-21T12:00:00+02\"}," +    
            "{\"field\": \"endTime\",\"before\": \"2016-01-20T18:00:00+02\",\"after\": \"2016-01-25T20:00:00+02\"}]," + 
            "\"candidates\":{\"edited\":[{\"id\":10},{\"id\":11}],\"added\":[{\"id\":13}],\"removed\":[{\"id\":12}]}}" +
            "}"));
    }

    @Test
    public void testParserWithNullInputs(){
        JSONObject result = parser.parse(null, null);
        assertNull("Parser doesn't have valid inputs", result);
    }
    
}
