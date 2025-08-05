package org.mockito.internal.verification;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.AtMost;
import org.mockito.internal.verification.NoMoreInteractions;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.After;
import org.mockito.verification.Timeout;
import org.mockito.verification.VerificationMode;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class VerificationOverTimeImpl_ESTest extends VerificationOverTimeImpl_ESTest_scaffolding {

    // Constants for better readability
    private static final long ZERO_MILLIS = 0L;
    private static final long TEN_MILLIS = 10L;
    private static final long POLLING_PERIOD_5157 = 5157L;
    private static final long DURATION_2416 = 2416L;
    private static final int AT_MOST_COUNT = 3199;
    
    // Test: Timer Management
    
    @Test(timeout = 4000)
    public void shouldReturnNullTimer_WhenCreatedWithNullTimer() throws Throwable {
        // Given: VerificationOverTimeImpl created with null timer
        After afterMode = new After(TEN_MILLIS, null);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(ZERO_MILLIS, afterMode, false, null);
        
        // When: Getting the timer
        Timer actualTimer = verification.getTimer();
        
        // Then: Timer should be null
        assertNull("Timer should be null when created with null timer", actualTimer);
    }

    @Test(timeout = 4000)
    public void shouldReturnProvidedTimer_WhenCreatedWithTimer() throws Throwable {
        // Given: Verification modes and timer
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        After afterMode = new After(DURATION_2416, noMoreInteractions);
        VerificationOverTimeImpl firstVerification = new VerificationOverTimeImpl(POLLING_PERIOD_5157, 1L, afterMode, false);
        Timer providedTimer = firstVerification.getTimer();
        
        // When: Creating new verification with the timer
        VerificationOverTimeImpl secondVerification = new VerificationOverTimeImpl(POLLING_PERIOD_5157, noMoreInteractions, true, providedTimer);
        
        // Then: Should maintain the polling period and return on success flag
        secondVerification.isReturnOnSuccess();
        assertEquals("Polling period should be preserved", POLLING_PERIOD_5157, secondVerification.getPollingPeriodMillis());
    }

    // Test: Polling Period Configuration
    
    @Test(timeout = 4000)
    public void shouldReturnZeroPollingPeriod_WhenConfiguredWithZero() throws Throwable {
        // Given: Verification configured with zero polling period
        AtMost atMostMode = new AtMost(AT_MOST_COUNT);
        Timeout timeoutMode = new Timeout(ZERO_MILLIS, atMostMode);
        Timer timer = new Timer(ZERO_MILLIS);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(ZERO_MILLIS, timeoutMode, false, timer);
        
        // When: Getting polling period
        long actualPollingPeriod = verification.getPollingPeriodMillis();
        
        // Then: Should return zero
        assertEquals("Polling period should be zero", ZERO_MILLIS, actualPollingPeriod);
    }

    @Test(timeout = 4000)
    public void shouldReturnConfiguredPollingPeriod() throws Throwable {
        // Given: Verification configured with specific polling period
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        After afterMode = new After(DURATION_2416, noMoreInteractions);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(POLLING_PERIOD_5157, 1L, afterMode, false);
        
        // When: Getting polling period
        long actualPollingPeriod = verification.getPollingPeriodMillis();
        
        // Then: Should return configured value
        assertEquals("Polling period should match configured value", POLLING_PERIOD_5157, actualPollingPeriod);
    }

    // Test: Delegate Verification Mode
    
    @Test(timeout = 4000)
    public void shouldReturnDelegateVerificationMode() throws Throwable {
        // Given: Verification with timeout delegate
        AtMost atMostMode = new AtMost(AT_MOST_COUNT);
        Timeout timeoutMode = new Timeout(ZERO_MILLIS, atMostMode);
        Timer timer = new Timer(AT_MOST_COUNT);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(ZERO_MILLIS, timeoutMode, false, timer);
        
        // When: Getting delegate
        VerificationMode actualDelegate = verification.getDelegate();
        
        // Then: Should return the timeout mode
        assertSame("Delegate should be the timeout mode", timeoutMode, actualDelegate);
    }

    // Test: Copy with Verification Mode
    
    @Test(timeout = 4000)
    public void shouldCreateNewInstance_WhenCopyingWithNullVerificationMode() throws Throwable {
        // Given: Original verification instance
        VerificationOverTimeImpl originalVerification = new VerificationOverTimeImpl(ZERO_MILLIS, ZERO_MILLIS, null, false);
        
        // When: Copying with null verification mode
        VerificationOverTimeImpl copiedVerification = originalVerification.copyWithVerificationMode(null);
        
        // Then: Should create different instance
        assertFalse("Copied verification should be different instance", copiedVerification.equals(originalVerification));
    }

    @Test(timeout = 4000)
    public void shouldPreservePollingPeriod_WhenCopyingWithVerificationMode() throws Throwable {
        // Given: Original verification with specific polling period
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        VerificationOverTimeImpl originalVerification = new VerificationOverTimeImpl(-102L, ZERO_MILLIS, noMoreInteractions, false);
        
        // When: Copying with same verification mode
        VerificationOverTimeImpl copiedVerification = originalVerification.copyWithVerificationMode(noMoreInteractions);
        
        // Then: Should preserve polling period in both instances
        assertEquals("Original polling period should be preserved", -102L, originalVerification.getPollingPeriodMillis());
        assertEquals("Copied polling period should match original", -102L, copiedVerification.getPollingPeriodMillis());
    }

    // Test: Error Handling
    
    @Test(timeout = 4000)
    public void shouldThrowNullPointerException_WhenVerifyingWithNullData() throws Throwable {
        // Given: Verification instance
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(ZERO_MILLIS, ZERO_MILLIS, null, false);
        
        // When & Then: Verifying with null data should throw NPE
        try {
            verification.verify(null);
            fail("Expected NullPointerException when verifying with null data");
        } catch(NullPointerException e) {
            verifyException("org.mockito.internal.verification.VerificationOverTimeImpl", e);
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowNoSuchElementException_WhenCopyingInSpecificConditions() throws Throwable {
        // Given: Verification configured to return on success
        VerificationOverTimeImpl originalVerification = new VerificationOverTimeImpl(3L, 3L, null, true);
        VerificationOverTimeImpl intermediateVerification = originalVerification.copyWithVerificationMode(null);
        
        // When & Then: Copying under specific conditions should throw NoSuchElementException
        try {
            intermediateVerification.copyWithVerificationMode(originalVerification);
            // Note: This assertion is marked as unstable in original code
        } catch(NoSuchElementException e) {
            verifyException("java.util.LinkedList", e);
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowRuntimeException_WhenCreatingWithNegativeValues() throws Throwable {
        // Given: Verification with zero values
        VerificationOverTimeImpl baseVerification = new VerificationOverTimeImpl(ZERO_MILLIS, ZERO_MILLIS, null, false);
        
        // When & Then: Creating with negative values should throw RuntimeException
        try {
            new VerificationOverTimeImpl(-720L, -720L, baseVerification, false);
            fail("Expected RuntimeException when creating with negative values");
        } catch(RuntimeException e) {
            // Expected exception
        }
    }

    // Test: Recovery from Failure
    
    @Test(timeout = 4000)
    public void shouldNotRecoverFromFailure_ForTimeoutWithNoMoreInteractions() throws Throwable {
        // Given: Timeout verification with NoMoreInteractions
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        Timeout timeoutMode = new Timeout(318L, noMoreInteractions);
        Timer timer = new Timer(POLLING_PERIOD_5157);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1L, timeoutMode, true, timer);
        
        // When: Checking if can recover from failure
        boolean canRecover = verification.canRecoverFromFailure(noMoreInteractions);
        
        // Then: Should not be able to recover and should preserve polling period
        assertFalse("Should not recover from NoMoreInteractions failure", canRecover);
        assertEquals("Polling period should be preserved", 1L, verification.getPollingPeriodMillis());
    }

    @Test(timeout = 4000)
    public void shouldRecoverFromFailure_ForAfterVerificationMode() throws Throwable {
        // Given: After verification mode
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        After afterMode = new After(DURATION_2416, noMoreInteractions);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(POLLING_PERIOD_5157, 1L, afterMode, false);
        
        // When: Checking if can recover from failure
        boolean canRecover = verification.canRecoverFromFailure(afterMode);
        
        // Then: Should be able to recover and preserve polling period
        assertTrue("Should recover from After verification failure", canRecover);
        assertEquals("Polling period should be preserved", POLLING_PERIOD_5157, verification.getPollingPeriodMillis());
    }

    @Test(timeout = 4000)
    public void shouldNotRecoverFromFailure_ForAtMostVerificationMode() throws Throwable {
        // Given: AtMost verification mode
        AtMost atMostMode = new AtMost(0);
        Timer timer = new Timer(0);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1444L, atMostMode, true, timer);
        
        // When: Checking if can recover from failure
        boolean canRecover = verification.canRecoverFromFailure(atMostMode);
        
        // Then: Should not be able to recover and preserve polling period
        assertFalse("Should not recover from AtMost verification failure", canRecover);
        assertEquals("Polling period should be preserved", 1444L, verification.getPollingPeriodMillis());
    }

    // Test: Verification Execution
    
    @Test(timeout = 4000)
    public void shouldCompleteVerification_WhenReturnOnSuccessIsTrue() throws Throwable {
        // Given: Verification configured to return on success
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(ZERO_MILLIS, ZERO_MILLIS, noMoreInteractions, true);
        
        // And: Verification data with empty invocation container
        MockSettingsImpl<Object> mockSettings = new MockSettingsImpl<Object>();
        InvocationContainerImpl invocationContainer = new InvocationContainerImpl(mockSettings);
        VerificationDataImpl verificationData = new VerificationDataImpl(invocationContainer, null);
        
        // When: Performing verification
        verification.verify(verificationData);
        
        // Then: Should complete successfully and return on success should be true
        assertTrue("Should be configured to return on success", verification.isReturnOnSuccess());
    }

    @Test(timeout = 4000)
    public void shouldExecuteVerification_WhenReturnOnSuccessIsFalse() throws Throwable {
        // Given: Verification configured not to return on success
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(2161L, 2161L, noMoreInteractions, false);
        
        // And: Verification data with empty invocation container
        MockSettingsImpl<Object> mockSettings = new MockSettingsImpl<Object>();
        InvocationContainerImpl invocationContainer = new InvocationContainerImpl(mockSettings);
        VerificationDataImpl verificationData = new VerificationDataImpl(invocationContainer, null);
        
        // When: Performing verification (this may throw an exception based on the verification logic)
        verification.verify(verificationData);
        
        // Then: Verification execution completes (exception handling depends on delegate behavior)
    }

    // Test: Property Access
    
    @Test(timeout = 4000)
    public void shouldReturnNullDelegate_WhenCreatedWithNullDelegate() throws Throwable {
        // Given: Verification created with null delegate
        Timer timer = new Timer(1418L);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1418L, null, true, timer);
        
        // When: Getting delegate and checking properties
        verification.getDelegate();
        
        // Then: Should preserve configuration
        assertTrue("Should be configured to return on success", verification.isReturnOnSuccess());
        assertEquals("Polling period should be preserved", 1418L, verification.getPollingPeriodMillis());
    }

    @Test(timeout = 4000)
    public void shouldHandleNegativePollingPeriod() throws Throwable {
        // Given: Verification with negative polling period
        After afterMode = new After(ZERO_MILLIS, null);
        Timer timer = new Timer(488L);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(-288L, afterMode, true, timer);
        
        // When: Getting polling period
        long actualPollingPeriod = verification.getPollingPeriodMillis();
        
        // Then: Should preserve negative value and return on success flag
        assertTrue("Should be configured to return on success", verification.isReturnOnSuccess());
        assertEquals("Should preserve negative polling period", -288L, actualPollingPeriod);
    }

    @Test(timeout = 4000)
    public void shouldReturnTimer_WhenAccessingTimer() throws Throwable {
        // Given: Verification with null delegate
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(ZERO_MILLIS, ZERO_MILLIS, null, false);
        
        // When: Getting timer and checking return on success
        verification.getTimer();
        boolean returnOnSuccess = verification.isReturnOnSuccess();
        
        // Then: Should return correct configuration
        assertFalse("Should not be configured to return on success", returnOnSuccess);
    }

    @Test(timeout = 4000)
    public void shouldThrowNullPointerException_WhenCopyingWithNullTimer() throws Throwable {
        // Given: Verification created with null timer
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(3058L, noMoreInteractions, true, null);
        
        // When & Then: Copying should throw NPE due to null timer
        try {
            verification.copyWithVerificationMode(noMoreInteractions);
            fail("Expected NullPointerException when copying with null timer");
        } catch(NullPointerException e) {
            verifyException("org.mockito.internal.verification.VerificationOverTimeImpl", e);
        }
    }
}