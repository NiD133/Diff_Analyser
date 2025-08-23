package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTestTest4 {

    // These values are not relevant to this test's logic, so they are named to reflect that.
    private static final long IRRELEVANT_POLLING_PERIOD = 10L;
    private static final long IRRELEVANT_DURATION = 1000L;

    @Mock
    private VerificationMode delegate;

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // The 'true' flag configures the class for "timeout" style verification.
        boolean returnOnSuccess = true;
        verificationOverTime =
                new VerificationOverTimeImpl(IRRELEVANT_POLLING_PERIOD, IRRELEVANT_DURATION, delegate, returnOnSuccess);
    }

    /**
     * Verifies that the polling mechanism only catches verification failures (AssertionError).
     * Any other exception, like a RuntimeException, should not be handled by the polling
     * logic and must be propagated immediately, as it indicates a setup or code issue
     * rather than a verification-in-progress failure.
     */
    @Test
    public void shouldImmediatelyRethrowNonAssertionErrorFromDelegate() {
        // Arrange
        RuntimeException unexpectedException = new RuntimeException("Delegate failed unexpectedly");
        doThrow(unexpectedException).when(delegate).verify(null);

        // Act & Assert
        assertThatThrownBy(() -> verificationOverTime.verify(null))
            .isSameAs(unexpectedException);
    }
}