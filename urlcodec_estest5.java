package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This test class contains an improved version of a test for the {@link URLCodec} class.
 * The original test was auto-generated and lacked clarity.
 */
// Note: The original class name 'URLCodec_ESTestTest5' is kept for consistency,
// but a name like 'URLCodecTest' would be more conventional.
public class URLCodec_ESTestTest5 extends URLCodec_ESTest_scaffolding {

    /**
     * Tests that the deprecated getEncoding() method returns null when the URLCodec
     * is instantiated with a null charset.
     */
    @Test
    public void getEncodingShouldReturnNullWhenConstructedWithNullCharset() {
        // Arrange: Create a URLCodec instance, passing null to the constructor
        // to explicitly set the charset to null. This is the correct way to
        // initialize the object's state for this test case.
        final URLCodec urlCodec = new URLCodec(null);

        // Act: Call the getEncoding() method to retrieve the charset.
        final String encoding = urlCodec.getEncoding();

        // Assert: Verify that the returned encoding is null, as expected.
        assertNull("The encoding should be null when the codec is constructed with a null charset.", encoding);
    }
}