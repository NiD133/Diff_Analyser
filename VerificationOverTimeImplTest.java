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

public class VerificationOverTimeImplTest {

    @Mock 
    private VerificationMode mockVerificationMode;
    
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        openMocks(this);
        // Initialize VerificationOverTimeImpl with a polling period of 10ms, a timeout of 1000ms,
        // and a mock delegate verification mode. It returns immediately on success.
        verificationOverTime = new VerificationOverTimeImpl(10, 1000, mockVerificationMode, true);
    }

    @Test
    public void shouldReturnOnSuccess() {
        // When verification is successful, it should delegate the call to the mockVerificationMode.
        verificationOverTime.verify(null);
        verify(mockVerificationMode).verify(null);
    }

    @Test
    public void shouldThrowMockitoAssertionError() {
        // Prepare a MockitoAssertionError to be thrown by the mockVerificationMode.
        MockitoAssertionError expectedError = new MockitoAssertionError("message");

        // Simulate the mockVerificationMode throwing the expected error during verification.
        doThrow(expectedError).when(mockVerificationMode).verify(null);

        // Verify that the same error is thrown by verificationOverTime.
        assertThatThrownBy(() -> verificationOverTime.verify(null))
                .isEqualTo(expectedError);
    }

    @Test
    public void shouldHandleJUnitAssertionError() {
        // Prepare an ArgumentsAreDifferent error to be thrown by the mockVerificationMode.
        ArgumentsAreDifferent expectedError = new ArgumentsAreDifferent("message", "wanted", "actual");

        // Simulate the mockVerificationMode throwing the expected error during verification.
        doThrow(expectedError).when(mockVerificationMode).verify(null);

        // Verify that the same error is thrown by verificationOverTime.
        assertThatThrownBy(() -> verificationOverTime.verify(null))
                .isEqualTo(expectedError);
    }

    @Test
    public void shouldNotWrapOtherExceptions() {
        // Prepare a RuntimeException to be thrown by the mockVerificationMode.
        RuntimeException expectedException = new RuntimeException();

        // Simulate the mockVerificationMode throwing the expected exception during verification.
        doThrow(expectedException).when(mockVerificationMode).verify(null);

        // Verify that the same exception is thrown by verificationOverTime.
        assertThatThrownBy(() -> verificationOverTime.verify(null))
                .isEqualTo(expectedException);
    }
}