package org.mockito.internal.verification;

import org.junit.Test;
import static org.junit.Assert.*;
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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class VerificationOverTimeImpl_ESTest extends VerificationOverTimeImpl_ESTest_scaffolding {

    private static final long TIMEOUT = 4000L;

    @Test(timeout = TIMEOUT)
    public void testTimerIsNullWhenVerificationModeIsNull() {
        After after = new After(10L, null);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0L, after, false, null);
        Timer timer = verification.getTimer();
        assertNull(timer);
    }

    @Test(timeout = TIMEOUT)
    public void testPollingPeriodIsSetCorrectly() {
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        After after = new After(2416L, noMoreInteractions);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(5157L, 1L, after, false);
        assertEquals(5157L, verification.getPollingPeriodMillis());
    }

    @Test(timeout = TIMEOUT)
    public void testPollingPeriodWithAtMostVerification() {
        AtMost atMost = new AtMost(3199);
        Timeout timeout = new Timeout(0L, atMost);
        Timer timer = new Timer(0L);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0L, timeout, false, timer);
        assertEquals(0L, verification.getPollingPeriodMillis());
    }

    @Test(timeout = TIMEOUT)
    public void testDelegateVerificationModeIsReturned() {
        AtMost atMost = new AtMost(3199);
        Timeout timeout = new Timeout(0L, atMost);
        Timer timer = new Timer(3199);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0L, timeout, false, timer);
        VerificationMode delegate = verification.getDelegate();
        assertSame(delegate, timeout);
    }

    @Test(timeout = TIMEOUT)
    public void testCopyWithVerificationModeCreatesNewInstance() {
        VerificationOverTimeImpl original = new VerificationOverTimeImpl(0L, 0L, null, false);
        VerificationOverTimeImpl copy = original.copyWithVerificationMode(null);
        assertFalse(copy.equals(original));
    }

    @Test(timeout = TIMEOUT)
    public void testCopyWithVerificationModePreservesPollingPeriod() {
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        VerificationOverTimeImpl original = new VerificationOverTimeImpl(-102L, 0L, noMoreInteractions, false);
        VerificationOverTimeImpl copy = original.copyWithVerificationMode(noMoreInteractions);
        assertEquals(-102L, copy.getPollingPeriodMillis());
    }

    @Test(timeout = TIMEOUT)
    public void testVerifyThrowsNullPointerExceptionWhenDataIsNull() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0L, 0L, null, false);
        try {
            verification.verify(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.mockito.internal.verification.VerificationOverTimeImpl", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCanRecoverFromFailureReturnsFalseForNoMoreInteractions() {
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        Timeout timeout = new Timeout(318L, noMoreInteractions);
        Timer timer = new Timer(5157L);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1L, timeout, true, timer);
        boolean canRecover = verification.canRecoverFromFailure(noMoreInteractions);
        assertEquals(1L, verification.getPollingPeriodMillis());
        assertFalse(canRecover);
    }

    @Test(timeout = TIMEOUT)
    public void testCanRecoverFromFailureReturnsTrueForAfter() {
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        After after = new After(2416L, noMoreInteractions);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(5157L, 1L, after, false);
        boolean canRecover = verification.canRecoverFromFailure(after);
        assertEquals(5157L, verification.getPollingPeriodMillis());
        assertTrue(canRecover);
    }

    @Test(timeout = TIMEOUT)
    public void testVerifyMethodWithValidData() {
        NoMoreInteractions noMoreInteractions = new NoMoreInteractions();
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0L, 0L, noMoreInteractions, true);
        MockSettingsImpl<Object> mockSettings = new MockSettingsImpl<>();
        InvocationContainerImpl invocationContainer = new InvocationContainerImpl(mockSettings);
        VerificationDataImpl verificationData = new VerificationDataImpl(invocationContainer, null);
        verification.verify(verificationData);
        assertTrue(verification.isReturnOnSuccess());
    }

    @Test(timeout = TIMEOUT)
    public void testGetDelegateReturnsNullWhenVerificationModeIsNull() {
        Timer timer = new Timer(1418L);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1418L, null, true, timer);
        verification.getDelegate();
        assertTrue(verification.isReturnOnSuccess());
        assertEquals(1418L, verification.getPollingPeriodMillis());
    }

    @Test(timeout = TIMEOUT)
    public void testNegativePollingPeriod() {
        After after = new After(0L, null);
        Timer timer = new Timer(488L);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(-288L, after, true, timer);
        assertEquals(-288L, verification.getPollingPeriodMillis());
        assertTrue(verification.isReturnOnSuccess());
    }

    @Test(timeout = TIMEOUT)
    public void testGetTimerReturnsNullWhenTimerIsNotSet() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0L, 0L, null, false);
        Timer timer = verification.getTimer();
        assertNull(timer);
        assertFalse(verification.isReturnOnSuccess());
    }
}