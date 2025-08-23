package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests for the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that encoding a null String returns null, which is a common contract
     * for encoder implementations.
     */
    @Test
    public void encodeStringShouldReturnNullForNullInput() {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        // The cast to (String) is necessary to resolve ambiguity between the
        // overloaded encode(String) and encode(Object) methods.
        String encodedString = codec.encode((String) null);

        // Assert
        assertNull("Encoding a null String should result in a null value.", encodedString);
    }
}