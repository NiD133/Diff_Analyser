package org.apache.commons.io.function;

import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#accept(IOIntConsumer, int)} correctly delegates
     * the call to the provided consumer when no exception is thrown.
     */
    @Test
    public void acceptWithIntConsumerShouldDelegateCall() throws IOException {
        // Arrange: Create a mock IOIntConsumer and define the input value.
        final IOIntConsumer mockConsumer = mock(IOIntConsumer.class);
        final int testValue = 111;

        // Act: Call the method under test.
        Uncheck.accept(mockConsumer, testValue);

        // Assert: Verify that the mock consumer's 'accept' method was called
        // exactly once with the correct value.
        verify(mockConsumer, times(1)).accept(testValue);
    }
}