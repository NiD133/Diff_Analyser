/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 *
 * These tests focus on the immediate success or failure propagation from the delegate
 * verification mode, rather than the timing and polling logic.
 */
public class VerificationOverTimeImplTest {

    // The specific values are not critical for these tests, as they don't test the timing aspect.
    private static final long DUMMY_POLLING_PERIOD = 10;
    private static final long DUMMY_DURATION = 1000;

    @Mock
    private VerificationMode delegate;

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        openMocks(this);
        // SUT is configured to return on success, mimicking the behavior of Mockito.timeout().
        boolean returnOnSuccess = true;
        verificationOverTime =
                new VerificationOverTimeImpl(
                        DUMMY_POLLING_PERIOD, DUMMY_DURATION, delegate, returnOnSuccess);
    }

    @Test
    public void verify_shouldPass_whenDelegateVerificationSucceeds() {
        // Arrange
        // The actual data is not relevant for this test's logic.
        VerificationData data = null;
        // The mock delegate will do nothing by default, which represents a successful verification.

        // Act
        verificationOverTime.verify(data);

        // Assert
        // Verify that the delegate's verify method was called as expected.
        verify(delegate).verify(data);
    }

    @Test
    public void verify_shouldRethrowMockitoAssertionError_whenDelegateFails() {
        // Arrange
        MockitoAssertionError delegateError = new MockitoAssertionError("Underlying verification failed");
        doThrow(delegateError).when(delegate).verify(null);

        // Act & Assert
        // The SUT should rethrow the exact same exception instance from the delegate.
        assertThatThrownBy(() -> verificationOverTime.verify(null)).isSameAs(delegateError);
    }

    @Test
    public void verify_shouldRethrowOpenTest4jError_whenDelegateFails() {
        // Arrange
        ArgumentsAreDifferent delegateError =
                new ArgumentsAreDifferent("Verification failed", "wanted", "actual");
        doThrow(delegateError).when(delegate).verify(null);

        // Act & Assert
        // The SUT should rethrow the exact same exception instance, even for non-Mockito assertion errors.
        assertThatThrownBy(() -> verificationOverTime.verify(null)).isSameAs(delegateError);
    }

    @Test
    public void verify_shouldPropagateOtherExceptions_whenDelegateThrowsThem() {
        // Arrange
        RuntimeException delegateException = new RuntimeException("An unexpected error occurred");
        doThrow(delegateException).when(delegate).verify(null);

        // Act & Assert
        // Non-AssertionError exceptions should be propagated immediately and not be wrapped.
        assertThatThrownBy(() -> verificationOverTime.verify(null)).isSameAs(delegateException);
    }
}