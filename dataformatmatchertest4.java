package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;
import static org.junit.jupiter.api.Assertions.*;

public class DataFormatMatcherTestTest4 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private final JsonFactory JSON_F = new JsonFactory();

    @Test
    void detectorConfiguration() {
        DataFormatDetector df0 = new DataFormatDetector(JSON_F);
        // Defaults are: SOLID for optimal, WEAK for minimum, so:
        assertSame(df0, df0.withOptimalMatch(MatchStrength.SOLID_MATCH));
        assertSame(df0, df0.withMinimalMatch(MatchStrength.WEAK_MATCH));
        assertSame(df0, df0.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD));
        // but will change
        assertNotSame(df0, df0.withOptimalMatch(MatchStrength.FULL_MATCH));
        assertNotSame(df0, df0.withMinimalMatch(MatchStrength.SOLID_MATCH));
        assertNotSame(df0, df0.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD + 5));
        // regardless, we should be able to use `toString()`
        assertNotNull(df0.toString());
    }
}
