package org.apache.commons.compress.harmony.pack200;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Tests for the {@link CodecEncoding} class.
 */
public class CodecEncodingTest {

    /**
     * Verifies that the CodecEncoding class can be instantiated successfully.
     *
     * Although this class seems to be a utility class with static methods,
     * this test ensures its constructor is accessible and executes without errors.
     */
    @Test
    public void shouldInstantiateCodecEncoding() {
        // Act: Attempt to create an instance of CodecEncoding.
        final CodecEncoding codecEncoding = new CodecEncoding();

        // Assert: Verify that the instance was created successfully.
        assertNotNull("The created CodecEncoding instance should not be null.", codecEncoding);
    }
}