package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;
import static org.junit.jupiter.api.Assertions.*;

public class DataFormatMatcherTestTest1 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private final JsonFactory JSON_F = new JsonFactory();

    @Test
    void getDataStream() throws IOException {
        byte[] byteArray = new byte[2];
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;
        DataFormatMatcher dataFormatMatcher = new DataFormatMatcher(null, byteArray, 1, 0, null, matchStrength);
        InputStream inputStream = dataFormatMatcher.getDataStream();
        assertEquals(0, inputStream.available());
        inputStream.close();
    }
}
