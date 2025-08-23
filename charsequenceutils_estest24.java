package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Test suite for the {@link CharSequenceUtils} class.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that calling {@code toCharArray} with a CharSequence that is too large
     * to fit in a char array throws an {@link OutOfMemoryError}.
     */
    @Test(expected = OutOfMemoryError.class)
    public void toCharArrayWithMaximumLengthCharSequenceShouldThrowOutOfMemoryError() {
        // Arrange: Create a mock CharSequence that reports a length that is too large
        // to be allocated as a char array. This is a robust way to test for
        // an OutOfMemoryError, as it does not depend on the specific heap size
        // of the JVM running the test.
        final CharSequence hugeCharSequence = new CharSequence() {
            @Override
            public int length() {
                // A length that will cause `new char[length]` to fail.
                return Integer.MAX_VALUE;
            }

            @Override
            public char charAt(final int index) {
                // This method should not be reached, as the array allocation fails first.
                throw new UnsupportedOperationException("charAt should not be called in this test.");
            }

            @Override
            public CharSequence subSequence(final int start, final int end) {
                // This method is not used by toCharArray.
                throw new UnsupportedOperationException("subSequence should not be called in this test.");
            }
        };

        // Act: This call is expected to throw an OutOfMemoryError when it tries to
        // allocate a new char array of size Integer.MAX_VALUE.
        CharSequenceUtils.toCharArray(hugeCharSequence);

        // Assert: The test is successful if an OutOfMemoryError is thrown,
        // which is handled by the `expected` attribute of the @Test annotation.
    }
}