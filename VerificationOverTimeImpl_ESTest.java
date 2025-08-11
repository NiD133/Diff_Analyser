package org.mockito.internal.verification;

import org.junit.Test;

import static org.junit.Assert.*;

import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.After;
import org.mockito.verification.Timeout;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    // region: constructor arguments and getters

    @Test
    public void exposesConstructorArgumentsViaGetters_whenConstructedWithTimer() {
        // Arrange
        VerificationMode delegate = new NoMoreInteractions();
        Timer timer = new Timer(0L);

        // Act
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(50L, delegate, true, timer);

        // Assert
        assertEquals(50L, mode.getPollingPeriodMillis());
        assertSame(delegate, mode.getDelegate());
        assertTrue(mode.isReturnOnSuccess());
        assertSame(timer, mode.getTimer());
    }

    @Test
    public void exposesConstructorArgumentsViaGetters_whenConstructedWithDuration() {
        // Arrange
        VerificationMode delegate = new NoMoreInteractions();

        // Act
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(25L, 200L, delegate, false);

        // Assert
        assertEquals(25L, mode.getPollingPeriodMillis());
        assertSame(delegate, mode.getDelegate());
        assertFalse(mode.isReturnOnSuccess());
        assertNotNull("Timer should be created when using the (polling, duration, ...) constructor", mode.getTimer());
    }

    @Test
    public void negativePollingPeriod_isPreservedAsGiven() {
        // Arrange
        VerificationMode delegate = new NoMoreInteractions();
        Timer timer = new Timer(0L);

        // Act
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(-5L, delegate, true, timer);

        // Assert
        assertEquals(-5L, mode.getPollingPeriodMillis());
    }

    @Test
    public void getTimer_returnsNull_whenProvidedNullTimer() {
        // Arrange
        VerificationMode delegate = new NoMoreInteractions();

        // Act
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(10L, delegate, false, null);

        // Assert
        assertNull(mode.getTimer());
    }

    // endregion

    // region: copyWithVerificationMode

    @Test
    public void copyWithVerificationMode_reusesTimingSettings_andReplacesDelegate() {
        // Arrange
        VerificationMode originalDelegate = new NoMoreInteractions();
        Timer timer = new Timer(0L);
        VerificationOverTimeImpl original = new VerificationOverTimeImpl(10L, originalDelegate, true, timer);

        VerificationMode newDelegate = new NoMoreInteractions();

        // Act
        VerificationOverTimeImpl copy = original.copyWithVerificationMode(newDelegate);

        // Assert
        assertNotSame("copy should be a new instance", original, copy);
        assertEquals(original.getPollingPeriodMillis(), copy.getPollingPeriodMillis());
        assertEquals(original.isReturnOnSuccess(), copy.isReturnOnSuccess());
        assertSame("Timer instance should be reused", original.getTimer(), copy.getTimer());
        assertSame("Delegate should be replaced", newDelegate, copy.getDelegate());
    }

    @Test
    public void copyWithVerificationMode_throwsNPE_whenTimerIsNull() {
        // Arrange
        VerificationMode delegate = new NoMoreInteractions();
        VerificationOverTimeImpl original = new VerificationOverTimeImpl(5L, delegate, true, null);

        // Act + Assert
        try {
            original.copyWithVerificationMode(new NoMoreInteractions());
            fail("Expected NullPointerException when copying with a null Timer");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    // endregion

    // region: canRecoverFromFailure (protected)

    @Test
    public void canRecoverFromFailure_returnsTrue_forAfter() {
        // Arrange
        VerificationMode delegate = new NoMoreInteractions();
        After after = new After(50L, delegate);
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(1L, delegate, false, new Timer(0L));

        // Act + Assert
        assertTrue(mode.canRecoverFromFailure(after));
    }

    @Test
    public void canRecoverFromFailure_returnsFalse_forAtMost() {
        // Arrange
        AtMost atMost = new AtMost(1);
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(1L, atMost, false, new Timer(0L));

        // Act + Assert
        assertFalse(mode.canRecoverFromFailure(atMost));
    }

    @Test
    public void canRecoverFromFailure_returnsFalse_forNoMoreInteractions() {
        // Arrange
        NoMoreInteractions noMore = new NoMoreInteractions();
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(1L, noMore, true, new Timer(0L));

        // Act + Assert
        assertFalse(mode.canRecoverFromFailure(noMore));
    }

    // endregion

    // region: verify

    @Test
    public void verify_nullData_throwsNullPointerException() {
        // Arrange
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(0L, 0L, new NoMoreInteractions(), false);

        // Act + Assert
        try {
            mode.verify((VerificationData) null);
            fail("Expected NullPointerException when verifying with null data");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    // This test only validates a trivial call path with a benign delegate;
    // it does not attempt to assert time-based behavior (which would be flaky).
    @Test
    public void verify_withNoMoreInteractions_andReturnOnSuccessTrue_doesNotChangeFlags() {
        // Arrange
        VerificationMode delegate = new NoMoreInteractions();
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(0L, 0L, delegate, true);

        // Act
        // Provide a minimal VerificationData stub; actual invocation content is irrelevant here.
        mode.verify(new org.mockito.internal.verification.VerificationDataImpl(
            new org.mockito.internal.stubbing.InvocationContainerImpl(new org.mockito.internal.creation.MockSettingsImpl<>()),
            null
        ));

        // Assert
        assertTrue(mode.isReturnOnSuccess());
    }

    // endregion

    // region: delegate exposure

    @Test
    public void getDelegate_returnsTheProvidedDelegate() {
        // Arrange
        VerificationMode timeout = new Timeout(0L, new AtMost(1));
        VerificationOverTimeImpl mode = new VerificationOverTimeImpl(0L, timeout, false, new Timer(0L));

        // Act + Assert
        assertSame(timeout, mode.getDelegate());
    }

    // endregion
}