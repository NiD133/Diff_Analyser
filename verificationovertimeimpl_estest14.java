package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Test suite for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * Verifies that the verification process completes successfully and immediately
     * when the delegate verification mode succeeds on the first try, using a zero-millisecond
     * timeout and polling period.
     */
    @Test
    public void verify_shouldSucceedImmediately_whenDelegateSucceedsWithZeroDuration() {
        // Arrange
        // A delegate mode that will succeed because there are no more interactions to verify.
        VerificationMode successfulDelegateMode = new NoMoreInteractions();

        // Configure VerificationOverTimeImpl with a zero duration and polling period.
        // This tests the edge case where the verification should not wait at all.
        long durationMillis = 0L;
        long pollingPeriodMillis = 0L;
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
                pollingPeriodMillis,
                durationMillis,
                successfulDelegateMode,
                returnOnSuccess
        );

        // Create verification data with no invocations, which satisfies the NoMoreInteractions delegate.
        InvocationContainerImpl emptyInvocationContainer = new InvocationContainerImpl(new MockSettingsImpl<>());
        VerificationData verificationData = new VerificationDataImpl(emptyInvocationContainer, (InvocationMatcher) null);

        // Act & Assert
        // The verify() method should execute without throwing an exception, which indicates a successful verification.
        // The test will fail if any MockitoAssertionError is thrown.
        verificationOverTime.verify(verificationData);
    }
}