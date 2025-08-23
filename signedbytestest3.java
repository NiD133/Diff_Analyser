package com.google.common.primitives;

import static com.google.common.truth.Truth.assertWithMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SignedBytes#compare(byte, byte)}.
 *
 * <p>This test focuses on verifying that {@code SignedBytes.compare()} behaves identically to the
 * standard {@code Byte.compare()}, as stated in its documentation.
 */
@RunWith(JUnit4.class)
public class SignedBytesCompareTest {

    private static final byte LEAST = Byte.MIN_VALUE;
    private static final byte GREATEST = Byte.MAX_VALUE;

    // A selection of byte values to test, including boundaries and common cases.
    private static final byte[] TEST_VALUES = {LEAST, -1, 0, 1, GREATEST};

    @Test
    public void compare_shouldBehaveIdenticallyToByteCompare() {
        // The contract of compare(a, b) is based on the sign of the result:
        // - negative if a < b
        // - positive if a > b
        // - zero if a == b
        // We verify this by comparing the signum of our implementation's result
        // against the signum of the standard JDK's result.

        for (byte a : TEST_VALUES) {
            for (byte b : TEST_VALUES) {
                int expectedSign = Integer.signum(Byte.compare(a, b));
                int actualSign = Integer.signum(SignedBytes.compare(a, b));

                assertWithMessage("Comparison of bytes (%s, %s) failed", a, b)
                        .that(actualSign)
                        .isEqualTo(expectedSign);
            }
        }
    }
}