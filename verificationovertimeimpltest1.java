package org.mockito.internal.verification;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    @Mock
    private VerificationMode delegate;

    private VerificationOverTimeImpl verificationWithTimeout;

    @Before
    public void setUp() {
        openMocks(this);

        // Configure VerificationOverTimeImpl to behave like Mockito.timeout(),
        // which returns immediately upon successful verification.
        long pollingPeriodMillis = 10;
        long durationMillis = 1000;
        boolean returnOnSuccess = true; // This flag makes it a "timeout" style verification
        verificationWithTimeout =
                new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
    }

    @Test
    public void shouldSucceedByDelegatingToWrappedVerificationMode() {
        // given
        // The delegate is a mock, so its verify() method will succeed by default without throwing an exception.
        VerificationData data = null;

        // when
        verificationWithTimeout.verify(data);

        // then
        // The primary behavior is to delegate the verification check to the wrapped mode.
        verify(delegate).verify(data);
    }
}