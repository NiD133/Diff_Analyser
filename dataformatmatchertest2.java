package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;
import static org.junit.jupiter.api.Assertions.*;

public class DataFormatMatcherTestTest2 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private final JsonFactory JSON_F = new JsonFactory();

    @Test
    void createsDataFormatMatcherTwo() throws IOException {
        try {
            @SuppressWarnings("unused")
            DataFormatMatcher dataFormatMatcher = new DataFormatMatcher(null, new byte[0], 2, 1, JSON_F, MatchStrength.NO_MATCH);
        } catch (IllegalArgumentException e) {
            verifyException(e, "Illegal start/length");
        }
    }
}
