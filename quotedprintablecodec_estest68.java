package org.apache.commons.codec.net;

import java.nio.charset.Charset;
import org.junit.Test;

/**
 * This test case verifies the behavior of the QuotedPrintableCodec
 * when it is constructed with a null Charset.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that calling getDefaultCharset() on a codec initialized with a null charset
     * throws a NullPointerException. This is expected because the method attempts to
     * access a method on the null charset field.
     */
    @Test(expected = NullPointerException.class)
    public void getDefaultCharsetShouldThrowNpeWhenConstructedWithNullCharset() {
        // Arrange: Create a codec instance, passing a null charset.
        // The constructor allows this, but subsequent method calls that use the
        // charset are expected to fail.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);

        // Act: Attempt to retrieve the default charset name.
        // This action is expected to throw the NullPointerException.
        codec.getDefaultCharset();

        // Assert: The test framework verifies that a NullPointerException was thrown,
        // as specified by the @Test(expected = ...) annotation.
    }
}