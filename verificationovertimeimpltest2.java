package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    private static final long REASONABLE_TIMEOUT = 100L;
    private static final long POLLING_PERIOD = 10L;

    @Mock
    private VerificationMode delegateVerification;

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        openMocks(this);
        // This configuration simulates a timeout() verification, which should
        // return immediately upon success.
        boolean returnOnSuccess = true;
        verificationOverTime =
                new VerificationOverTimeImpl(
                        POLLING_PERIOD, REASONABLE_TIMEOUT, delegateVerification, returnOnSuccess);
    }

    @Test
    public void shouldRethrowDelegateErrorWhenVerificationConsistentlyFails() {
        // Arrange: Configure the delegate verification mode to always fail.
        MockitoAssertionError delegateError = new MockitoAssertionError("Verification failed");
        doThrow(delegateError).when(delegateVerification).verify(null);

        // Act & Assert: Verify that the SUT, after polling and timing out,
        // rethrows the exact error instance from the delegate.
        assertThatThrownBy(() -> verificationOverTime.verify(null))
            .isSameAs(delegateError);
    }
}