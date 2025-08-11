/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VerificationOverTimeImplTest {

    private static final long POLLING_PERIOD_MS = 10;
    private static final long DURATION_MS = 1000;

    @Mock
    private VerificationMode delegate;

    private VerificationOverTimeImpl impl;

    @Before
    public void setUp() {
        impl = new VerificationOverTimeImpl(POLLING_PERIOD_MS, DURATION_MS, delegate, true);
    }

    @Test
    public void shouldSucceedWhenDelegateSucceeds() {
        // Given: No setup needed for successful verification
        
        // When: Performing verification
        impl.verify(null);
        
        // Then: Delegate is invoked successfully
        verify(delegate).verify(null);
    }

    @Test
    public void shouldPropagateMockitoAssertionError() {
        // Given: Delegate configured to throw MockitoAssertionError
        MockitoAssertionError expectedError = new MockitoAssertionError("message");
        doThrow(expectedError).when(delegate).verify(null);

        // When/Then: Verify exception propagation
        assertThatThrownBy(() -> impl.verify(null))
                .isSameAs(expectedError);
    }

    @Test
    public void shouldPropagateArgumentsAreDifferentError() {
        // Given: Delegate configured to throw ArgumentsAreDifferent
        ArgumentsAreDifferent expectedError = 
            new ArgumentsAreDifferent("message", "wanted", "actual");
        doThrow(expectedError).when(delegate).verify(null);

        // When/Then: Verify exception propagation
        assertThatThrownBy(() -> impl.verify(null))
                .isSameAs(expectedError);
    }

    @Test
    public void shouldPropagateRuntimeExceptionUnwrapped() {
        // Given: Delegate configured to throw RuntimeException
        RuntimeException expectedException = new RuntimeException("error");
        doThrow(expectedException).when(delegate).verify(null);

        // When/Then: Verify exception propagation
        assertThatThrownBy(() -> impl.verify(null))
                .isSameAs(expectedException);
    }
}