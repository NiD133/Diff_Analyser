package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * This test verifies that the getPollingPeriodMillis() method correctly returns
     * the polling period value that was provided in the constructor.
     */
    @Test
    public void shouldReturnPollingPeriodSetInConstructor() {
        // Arrange
        final long expectedPollingPeriod = 50L;
        final long duration = 100L;
        final VerificationMode delegate = new NoMoreInteractions();

        VerificationOverTimeImpl verificationOverTime =
            new VerificationOverTimeImpl(expectedPollingPeriod, duration, delegate, false);

        // Act
        long actualPollingPeriod = verificationOverTime.getPollingPeriodMillis();

        // Assert
        assertEquals(expectedPollingPeriod, actualPollingPeriod);
    }
}