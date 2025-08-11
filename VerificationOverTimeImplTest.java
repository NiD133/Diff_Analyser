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
import org.mockito.verification.VerificationMode;

/**
 * Tests for VerificationOverTimeImpl focused on:
 * - Delegation to the underlying VerificationMode
 * - Propagation of exceptions without wrapping
 *
 * Note: We pass null as VerificationData because these tests only verify delegation and exception handling.
 */
public class VerificationOverTimeImplTest {

    private static final long POLLING_MS = 10L;
    private static final long DURATION_MS = 1_000L;

    @Mock
    private VerificationMode delegateVerificationMode;

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        openMocks(this);
        verificationOverTime = new VerificationOverTimeImpl(
                POLLING_MS, DURATION_MS, delegateVerificationMode, /*returnOnSuccess=*/ true);
    }

    @Test
    public void delegates_verification_to_the_underlying_mode() {
        // When
        verificationOverTime.verify(null); // VerificationData is irrelevant for these tests

        // Then
        verify(delegateVerificationMode).verify(null);
    }

    @Test
    public void propagates_MockitoAssertionError_unwrapped() {
        MockitoAssertionError expected = new MockitoAssertionError("message");
        assertExceptionIsPropagatedAsIs(expected);
    }

    @Test
    public void propagates_AssertionError_subclasses_unwrapped() {
        ArgumentsAreDifferent expected = new ArgumentsAreDifferent("message", "wanted", "actual");
        assertExceptionIsPropagatedAsIs(expected);
    }

    @Test
    public void propagates_runtime_exceptions_unwrapped() {
        RuntimeException expected = new RuntimeException("boom");
        assertExceptionIsPropagatedAsIs(expected);
    }

    private void assertExceptionIsPropagatedAsIs(Throwable expected) {
        // Given
        doThrow(expected).when(delegateVerificationMode).verify(null);

        // When / Then
        assertThatThrownBy(() -> verificationOverTime.verify(null))
                .isSameAs(expected); // ensure the exact same instance is thrown (no wrapping)
    }
}