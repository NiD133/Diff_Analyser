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
 * Tests for VerificationOverTimeImpl, which handles time-based verification modes
 * like timeout() and after() by polling a delegate verification mode over time.
 */
public class VerificationOverTimeImplTest {
    
    private static final long POLLING_PERIOD_MS = 10;
    private static final long TIMEOUT_DURATION_MS = 1000;
    private static final boolean RETURN_ON_SUCCESS = true;
    
    @Mock 
    private VerificationMode mockDelegate;
    
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        openMocks(this);
        verificationOverTime = new VerificationOverTimeImpl(
            POLLING_PERIOD_MS, 
            TIMEOUT_DURATION_MS, 
            mockDelegate, 
            RETURN_ON_SUCCESS
        );
    }

    @Test
    public void shouldSucceedWhenDelegateVerificationPasses() {
        // Given: delegate verification will succeed (no exception thrown)
        
        // When: verification is performed
        verificationOverTime.verify(null);
        
        // Then: delegate should have been called
        verify(mockDelegate).verify(null);
    }

    @Test
    public void shouldPropagateOriginalMockitoAssertionError() {
        // Given: delegate throws a MockitoAssertionError
        MockitoAssertionError expectedError = new MockitoAssertionError("Verification failed");
        doThrow(expectedError).when(mockDelegate).verify(null);
        
        // When & Then: the same error should be thrown without modification
        assertThatThrownBy(() -> verificationOverTime.verify(null))
                .isEqualTo(expectedError);
    }

    @Test
    public void shouldPropagateJUnitAssertionErrorsUnchanged() {
        // Given: delegate throws a JUnit-style assertion error (ArgumentsAreDifferent)
        ArgumentsAreDifferent junitError = new ArgumentsAreDifferent(
            "Arguments are different", 
            "expected argument", 
            "actual argument"
        );
        doThrow(junitError).when(mockDelegate).verify(null);
        
        // When & Then: the JUnit error should be propagated as-is
        assertThatThrownBy(() -> verificationOverTime.verify(null))
                .isEqualTo(junitError);
    }

    @Test
    public void shouldNotWrapNonAssertionExceptions() {
        // Given: delegate throws a non-assertion runtime exception
        RuntimeException unexpectedException = new RuntimeException("Unexpected error");
        doThrow(unexpectedException).when(mockDelegate).verify(null);
        
        // When & Then: the runtime exception should be propagated without wrapping
        assertThatThrownBy(() -> verificationOverTime.verify(null))
                .isEqualTo(unexpectedException);
    }
}