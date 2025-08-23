package org.apache.commons.compress.utils;

import org.junit.Test;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    @Test
    public void positionShouldThrowIllegalArgumentExceptionForNegativePosition() {
        // Arrange
        // The specific channels used do not matter for this test, so an empty list is sufficient.
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(Collections.emptyList());
        long negativePosition = -678L;

        // Act & Assert
        // Expect an IllegalArgumentException when trying to set a negative position.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> channel.position(negativePosition)
        );

        // Verify that the exception message is informative.
        String expectedMessage = "Negative position: " + negativePosition;
        assertEquals(expectedMessage, exception.getMessage());
    }
}