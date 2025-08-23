package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void isActuallyWhitespaceRecognizesTabAsWhitespace() {
        // The character code 9 corresponds to the tab character ('\t').
        // The isActuallyWhitespace method should identify it as whitespace.
        assertTrue("The tab character should be classified as whitespace.", StringUtil.isActuallyWhitespace('\t'));
    }
}