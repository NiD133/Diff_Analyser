package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 * This test case focuses on the {@code isActuallyWhitespace} method.
 *
 * Note: The original test extended a scaffolding class, which is preserved here.
 * In a full refactoring, the purpose of this scaffolding would be reviewed.
 */
public class StringUtilTest extends StringUtil_ESTest_scaffolding {

    /**
     * Verifies that the carriage return character ('\r') is correctly identified
     * as "actual whitespace". The integer code point for '\r' is 13.
     */
    @Test
    public void carriageReturnIsConsideredActualWhitespace() {
        // The isActuallyWhitespace method should return true for the carriage return character.
        // Using the character literal '\r' is more readable than its integer value 13.
        assertTrue(
            "The carriage return character ('\\r') should be classified as whitespace.",
            StringUtil.isActuallyWhitespace('\r')
        );
    }
}