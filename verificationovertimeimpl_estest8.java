package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImpl_ESTestTest8 extends VerificationOverTimeImpl_ESTest_scaffolding {

    /**
     * Verifies that the verify() method throws a NullPointerException
     * when the delegate VerificationMode is null.
     *
     * This is the expected behavior, as the implementation relies on the delegate
     * to perform the actual verification logic.
     */
    @Test(expected = NullPointerException.class)
    public void verifyShouldThrowNullPointerExceptionWhenDelegateIsNull() {
        // Arrange: Create an instance with a null delegate.
        // The other parameters are not relevant to this specific failure case.
        long anyPollingPeriod = 0L;
        long anyDuration = 0L;
        VerificationMode nullDelegate = null;
        boolean returnOnSuccess = false;

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
            anyPollingPeriod,
            anyDuration,
            nullDelegate,
            returnOnSuccess
        );

        // The VerificationData can also be null, as the exception is thrown
        // before this parameter is used.
        VerificationData data = null;

        // Act & Assert: Calling verify() should immediately throw a NullPointerException
        // because the delegate is null. The @Test(expected=...) annotation handles the assertion.
        verificationOverTime.verify(data);
    }
}