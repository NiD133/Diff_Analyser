package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * This test verifies the behavior of the copyWithVerificationMode method
     * when the VerificationOverTimeImpl instance is created with a null Timer.
     *
     * It ensures that the method correctly throws a NullPointerException,
     * which is the expected behavior when an internal dependency (the timer) is null
     * and its methods are subsequently accessed.
     */
    @Test
    public void copyWithVerificationMode_whenConstructedWithNullTimer_throwsNullPointerException() {
        // Arrange
        // A dummy verification mode is needed for the constructor and method arguments.
        VerificationMode dummyDelegateMode = new NoMoreInteractions();
        long anyPollingPeriod = 100L;
        boolean returnOnSuccess = true;

        // Create the object under test with a null Timer. This is the specific
        // condition we want to test.
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
            anyPollingPeriod,
            dummyDelegateMode,
            returnOnSuccess,
            null // Pass a null Timer
        );

        // Act & Assert
        // We expect a NullPointerException because the copyWithVerificationMode method
        // likely attempts to use the null timer object internally.
        assertThrows(NullPointerException.class, () -> {
            verificationOverTime.copyWithVerificationMode(dummyDelegateMode);
        });
    }
}