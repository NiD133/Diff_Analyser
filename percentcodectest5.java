package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

// The test class name is simplified for clarity and follows standard conventions.
class PercentCodecTest {

    @Test
    // The test method name clearly describes the scenario under test:
    // the method being tested, the input condition, and the expected outcome.
    void decodeObjectWithNullInputShouldReturnNull() throws DecoderException {
        // Arrange: Set up the object under test.
        final PercentCodec codec = new PercentCodec();

        // Act: Call the method with a null input.
        // The cast to (Object) is necessary to resolve ambiguity between the
        // overloaded decode(byte[]) and decode(Object) methods.
        final Object result = codec.decode((Object) null);

        // Assert: Verify that the result is null, as expected.
        assertNull(result);
    }
}