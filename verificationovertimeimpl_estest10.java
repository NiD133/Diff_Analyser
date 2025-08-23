package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;

/**
 * Tests for the {@link VerificationOverTimeImpl} class.
 */
public class VerificationOverTimeImplTest {

    /**
     * Verifies that the constructor throws an exception if a negative duration is provided.
     * The constructor relies on an internal Timer, which cannot be initialized with a
     * negative duration. This test ensures that such invalid input is properly rejected.
     */
    @Test(expected = MockitoException.class)
    public void constructorShouldThrowExceptionForNegativeDuration() {
        // Arrange: A dummy delegate is required by the constructor, but its behavior is irrelevant.
        VerificationMode dummyDelegate = mock(VerificationMode.class);
        long negativeDuration = -720L;
        long anyPollingPeriod = -720L; // Value is not critical, as the duration check happens first.

        // Act & Assert: Attempting to create an instance with a negative duration
        // should immediately throw a MockitoException.
        new VerificationOverTimeImpl(anyPollingPeriod, negativeDuration, dummyDelegate, false);
    }
}