package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertThrows;

/**
 * This test case focuses on how VerificationOverTimeImpl handles exceptions
 * from its delegate verification mode.
 */
public class VerificationOverTimeImplTest {

    /**
     * Verifies that the verify() method propagates a NullPointerException when the
     * delegated VerificationMode (NoMoreInteractions) fails due to invalid VerificationData.
     *
     * <p><b>Scenario:</b> The {@link VerificationData} is created with a null {@code InvocationMatcher}.
     * The {@link NoMoreInteractions} mode does not support this and will throw a
     * {@code NullPointerException} when its verify method is called. This test ensures that
     * {@code VerificationOverTimeImpl} correctly propagates this underlying exception.
     */
    @Test
    public void verifyShouldPropagateNPEWhenDelegateFailsOnNullInvocationMatcher() {
        // Arrange
        long durationMillis = 100L;
        long pollingPeriodMillis = 10L;
        VerificationMode delegateMode = new NoMoreInteractions();

        // The system under test, configured to poll the delegate mode.
        VerificationOverTimeImpl verificationOverTime =
            new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegateMode, false);

        // Create verification data with a null InvocationMatcher, which will cause the delegate to fail.
        InvocationContainerImpl emptyInvocationContainer = new InvocationContainerImpl(new MockSettingsImpl<>());
        VerificationDataImpl verificationDataWithNullMatcher = new VerificationDataImpl(emptyInvocationContainer, null);

        // Act & Assert
        // We expect a NullPointerException because the NoMoreInteractions delegate cannot handle
        // a null InvocationMatcher from the verification data.
        assertThrows(NullPointerException.class, () -> {
            verificationOverTime.verify(verificationDataWithNullMatcher);
        });
    }
}