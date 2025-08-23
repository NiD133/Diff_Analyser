package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.After;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertNull;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * This test verifies that the getTimer() method correctly returns the
     * Timer instance that was passed into the constructor. In this specific
     * case, it ensures that a null timer is handled correctly.
     */
    @Test
    public void getTimerShouldReturnTheTimerInstanceProvidedInTheConstructor() {
        // Arrange
        // The specific delegate mode and other parameters are not relevant for this test.
        VerificationMode dummyDelegate = new After(10L, null);

        // Create the instance under test, passing a null timer.
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
                0L,             // pollingPeriodMillis
                dummyDelegate,
                false,          // returnOnSuccess
                null            // timer
        );

        // Act
        Timer retrievedTimer = verificationOverTime.getTimer();

        // Assert
        assertNull("getTimer() should return the null instance that was provided in the constructor.", retrievedTimer);
    }
}