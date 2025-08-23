package org.apache.commons.codec.net;

import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the static methods of the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecStaticTest {

    /**
     * Tests that encodeQuotedPrintable returns null when the provided BitSet of
     * printable characters is null. This is a guard condition, and the method
     * should handle this gracefully regardless of other parameters.
     */
    @Test
    public void encodeQuotedPrintableWithNullBitSetShouldReturnNull() {
        // Arrange: Create a sample byte array to be encoded. Its content is irrelevant
        // for this test, as the null BitSet is the primary condition being tested.
        final byte[] inputBytes = "any string".getBytes();
        final boolean strictMode = true;

        // Act: Call the method with a null BitSet.
        final byte[] result = QuotedPrintableCodec.encodeQuotedPrintable((BitSet) null, inputBytes, strictMode);

        // Assert: Verify that the method returns null as expected.
        assertNull("The result should be null when the printable character BitSet is null.", result);
    }
}