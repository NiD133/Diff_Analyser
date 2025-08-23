package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;
import static org.junit.jupiter.api.Assertions.*;

public class DataFormatMatcherTestTest3 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private final JsonFactory JSON_F = new JsonFactory();

    @Test
    void getMatchedFormatNameReturnsNameWhenMatches() {
        DataFormatMatcher dataFormatMatcher = new DataFormatMatcher(null, new byte[2], 1, 0, JSON_F, MatchStrength.SOLID_MATCH);
        assertEquals(JsonFactory.FORMAT_NAME_JSON, dataFormatMatcher.getMatchedFormatName());
    }
}
