package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.verification.VerificationMode;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    // Constants to clarify that timing values are not relevant for this specific test case,
    // as the failure is expected to be immediate.
    private static final long IRRELEVANT_POLLING_PERIOD = 10L;
    private static final long IRRELEVANT_DURATION = 1000L;

    @Mock
    private VerificationMode delegate;

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        openMocks(this);
        verificationOverTime = new VerificationOverTimeImpl(IRRELEVANT_POLLING_PERIOD, IRRELEVANT_DURATION, delegate, true);
    }

    @Test
    public void shouldPropagateUnderlyingVerificationErrorWithoutWrapping() {
        // given: The delegate verification mode fails with a specific assertion error.
        // This simulates a typical Mockito verification failure, like `verify(mock).method("wrong arg")`.
        ArgumentsAreDifferent verificationError = new ArgumentsAreDifferent("Verification failed", "wanted", "actual");
        doThrow(verificationError).when(delegate).verify(null);

        // when: The verification over time is executed.
        // then: It should re-throw the exact same exception instance from the delegate.
        // This ensures the original error details are preserved and not hidden inside a wrapper exception.
        assertThatThrownBy(() -> verificationOverTime.verify(null))
            .isSameAs(verificationError);
    }
}