package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.Timeout;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertSame;

/**
 * Test for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    @Test
    public void getDelegate_shouldReturnTheVerificationModePassedToConstructor() {
        // Arrange
        // Create a delegate VerificationMode. Its specific type and configuration
        // are not important for this test, as we only check if the getter returns it.
        VerificationMode expectedDelegate = new Timeout(100L, new AtMost(5));
        Timer timer = new Timer(100L);

        // Create the object under test, passing the delegate to its constructor.
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
                0L,    // pollingPeriod (not relevant for this test)
                expectedDelegate,
                false, // returnOnSuccess (not relevant for this test)
                timer
        );

        // Act
        VerificationMode actualDelegate = verificationOverTime.getDelegate();

        // Assert
        assertSame(
                "getDelegate() should return the exact same VerificationMode instance that was passed to the constructor.",
                expectedDelegate,
                actualDelegate
        );
    }
}