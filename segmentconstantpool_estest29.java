package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for the {@link SegmentConstantPool} class.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that the {@code regexMatches} method throws a NullPointerException
     * when the string to be compared against the regex is null.
     */
    @Test(expected = NullPointerException.class)
    public void regexMatchesShouldThrowNPEForNullInputString() {
        // The method under test is expected to throw a NullPointerException
        // because the second argument, the string to be matched, is null.
        SegmentConstantPool.regexMatches("^<init>.*", null);
    }
}