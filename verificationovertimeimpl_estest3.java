package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    @Test
    public void shouldReturnPollingPeriodSetInConstructor() {
        // Arrange
        final long expectedPollingPeriod = 0L;
        
        // These dependencies are required by the constructor but their specific state
        // is irrelevant for testing the polling period getter.
        VerificationMode dummyDelegate = mock(VerificationMode.class);
        Timer dummyTimer = new Timer(100L);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
                expectedPollingPeriod,
                dummyDelegate,
                false, // The 'returnOnSuccess' flag does not affect this test.
                dummyTimer);

        // Act
        long actualPollingPeriod = verificationOverTime.getPollingPeriodMillis();

        // Assert
        assertEquals(expectedPollingPeriod, actualPollingPeriod);
    }
}