package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
// Note: The original test class name 'VerificationOverTimeImpl_ESTestTest16' 
// was kept for context, but would typically be renamed to 'VerificationOverTimeImplTest'.
public class VerificationOverTimeImpl_ESTestTest16 {

    /**
     * Verifies that the constructor correctly initializes the object's properties
     * and that the corresponding getters return the configured values.
     */
    @Test
    public void shouldCorrectlyStorePropertiesFromConstructor() {
        // Arrange
        long expectedPollingPeriod = 1418L;
        VerificationMode delegateMode = null; // The delegate can be null
        boolean shouldReturnOnSuccess = true;
        Timer timer = new Timer(expectedPollingPeriod);

        // Act
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
            expectedPollingPeriod,
            delegateMode,
            shouldReturnOnSuccess,
            timer
        );

        // Assert
        // Verify that all constructor arguments are correctly stored and accessible via getters.
        assertEquals(expectedPollingPeriod, verificationOverTime.getPollingPeriodMillis());
        assertTrue("Should be configured to return on success", verificationOverTime.isReturnOnSuccess());
        assertNull("The delegate verification mode should be null as configured", verificationOverTime.getDelegate());
        assertSame("The timer instance should be the one passed to the constructor", timer, verificationOverTime.getTimer());
    }
}