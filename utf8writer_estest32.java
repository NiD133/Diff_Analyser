package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on its constants and internal logic.
 */
public class UTF8WriterTest {

    /**
     * Verifies that the public constant {@code SURROGATE_BASE} has the correct,
     * pre-calculated value. This constant is crucial for correctly encoding
     * surrogate pairs in UTF-8.
     */
    @Test
    public void surrogateBaseConstant_shouldHaveCorrectValue() {
        // This test verifies the value of a static constant. The original test included
        // unnecessary setup for an IOContext and a UTF8Writer instance, which has been
        // removed to improve clarity and focus.

        // The expected value is derived from the formula used in the source class:
        // 0x10000 - SURR2_FIRST - (SURR1_FIRST << 10)
        // where SURR1_FIRST = 0xD800 and SURR2_FIRST = 0xDC00.
        // Calculation: 65536 - 56320 - (55296 << 10) = 9216 - 56623104 = -56613888
        final int expectedSurrogateBase = -56613888;

        assertEquals("The SURROGATE_BASE constant should match its defined formula.",
                expectedSurrogateBase, UTF8Writer.SURROGATE_BASE);
    }
}