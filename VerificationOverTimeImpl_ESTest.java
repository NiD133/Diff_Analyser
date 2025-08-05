import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.AtMost;
import org.mockito.internal.verification.NoMoreInteractions;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.After;
import org.mockito.verification.Timeout;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VerificationOverTimeImpl}.
 *
 * These tests cover the behavior of the VerificationOverTimeImpl class, which adds polling
 * and timeout/delay capabilities to a delegate VerificationMode.
 */
public class VerificationOverTimeImplTest {

    // --- Constructor and State Tests ---

    @Test
    public void shouldCorrectlyStorePropertiesWhenConstructed() {
        // Arrange
        VerificationMode delegateMode = new AtMost(5);
        long pollingPeriod = 10L;
        long duration = 100L;

        // Act
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriod, duration, delegateMode, true);

        // Assert
        assertEquals("Polling period should be stored", pollingPeriod, verification.getPollingPeriodMillis());
        assertEquals("Delegate verification mode should be stored", delegateMode, verification.getDelegate());
        assertTrue("ReturnOnSuccess flag should be stored", verification.isReturnOnSuccess());
        assertNotNull("A timer should be created internally", verification.getTimer());
    }

    @Test
    public void shouldAcceptPreconfiguredTimerInConstructor() {
        // Arrange
        VerificationMode delegateMode = new NoMoreInteractions();
        Timer preconfiguredTimer = new Timer(500L);

        // Act
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(20L, delegateMode, false, preconfiguredTimer);

        // Assert
        assertSame("Should use the provided timer instance", preconfiguredTimer, verification.getTimer());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenConstructedWithNegativeDuration() {
        // Arrange
        VerificationMode delegateMode = new NoMoreInteractions();

        // Act & Assert
        new VerificationOverTimeImpl(10L, -100L, delegateMode, false);
    }

    // --- copyWithVerificationMode() Tests ---

    @Test
    public void shouldCreateCopyOfInstanceWithNewDelegate() {
        // Arrange
        VerificationMode initialDelegate = new AtMost(3);
        Timer timer = new Timer(100L);
        VerificationOverTimeImpl originalVerification = new VerificationOverTimeImpl(10L, initialDelegate, true, timer);

        VerificationMode newDelegate = new NoMoreInteractions();

        // Act
        VerificationOverTimeImpl copiedVerification = originalVerification.copyWithVerificationMode(newDelegate);

        // Assert
        assertNotSame("Should create a new instance", originalVerification, copiedVerification);
        assertEquals("New instance should have the new delegate", newDelegate, copiedVerification.getDelegate());

        // Verify that other properties are copied from the original
        assertEquals("Polling period should be copied", originalVerification.getPollingPeriodMillis(), copiedVerification.getPollingPeriodMillis());
        assertEquals("ReturnOnSuccess flag should be copied", originalVerification.isReturnOnSuccess(), copiedVerification.isReturnOnSuccess());
        assertSame("Timer instance should be copied", originalVerification.getTimer(), copiedVerification.getTimer());
    }

    @Test(expected = NullPointerException.class)
    public void copyWithVerificationModeShouldThrowNPEIfConstructedWithNullTimer() {
        // Arrange
        // This constructor variant allows a null timer, which can cause issues when copying.
        VerificationOverTimeImpl verificationWithNullTimer = new VerificationOverTimeImpl(50L, new NoMoreInteractions(), true, null);

        // Act & Assert
        verificationWithNullTimer.copyWithVerificationMode(new AtMost(1));
    }

    // --- canRecoverFromFailure() Tests ---

    @Test
    public void shouldBeAbleToRecoverFromFailureForAfterVerificationMode() {
        // Arrange
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, 100L, null, false);
        VerificationMode afterMode = new After(100L, new AtMost(1));

        // Act
        boolean canRecover = verification.canRecoverFromFailure(afterMode);

        // Assert
        assertTrue("Should recover from After, as it's expected to fail initially and succeed later", canRecover);
    }

    @Test
    public void shouldNotBeAbleToRecoverFromFailureForAtMostVerificationMode() {
        // Arrange
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, 100L, null, false);
        VerificationMode atMostMode = new AtMost(1);

        // Act
        boolean canRecover = verification.canRecoverFromFailure(atMostMode);

        // Assert
        assertFalse("Should not recover from AtMost, as exceeding the count is a final failure", canRecover);
    }

    @Test
    public void shouldNotBeAbleToRecoverFromFailureForTimeoutVerificationMode() {
        // Arrange
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, 100L, null, false);
        VerificationMode timeoutMode = new Timeout(100L, new AtMost(1));

        // Act
        boolean canRecover = verification.canRecoverFromFailure(timeoutMode);

        // Assert
        assertFalse("Should not recover from Timeout, as it's a wrapper that shouldn't be retried itself", canRecover);
    }

    // --- verify() Method Tests ---

    @Test(expected = NullPointerException.class)
    public void verifyShouldThrowNPEWhenVerificationDataIsNull() {
        // Arrange
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, 100L, new NoMoreInteractions(), false);

        // Act & Assert
        verification.verify(null);
    }

    @Test
    public void verifyShouldPassWhenDelegateSucceedsAndReturnOnSuccessIsTrue() {
        // Arrange
        VerificationMode succeedingDelegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, 100L, succeedingDelegate, true);

        // Act & Assert
        try {
            verification.verify(data);
            // Success: no exception was thrown.
        } catch (MockitoAssertionError e) {
            fail("Verification should have passed, but it threw an error: " + e.getMessage());
        }

        // Verify that the delegate was called once, as it succeeded immediately.
        verify(succeedingDelegate, times(1)).verify(data);
    }

    @Test
    public void verifyShouldPollUntilTimeoutWhenDelegateSucceedsAndReturnOnSuccessIsFalse() {
        // Arrange
        VerificationMode succeedingDelegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);

        // Configure the timer to simulate polling 3 times before the duration ends.
        when(timer.isCounting()).thenReturn(true, true, true, false);

        // returnOnSuccess is false, so it should keep polling even after success.
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, succeedingDelegate, false, timer);

        // Act
        verification.verify(data);

        // Assert
        // The delegate's verify() should be called 3 times, once for each polling cycle.
        verify(succeedingDelegate, times(3)).verify(data);
        verify(timer, times(1)).start();
    }

    @Test
    public void verifyShouldFailImmediatelyWhenUnrecoverableDelegateFails() {
        // Arrange
        // NoMoreInteractions is an "unrecoverable" mode. If it fails once, it will always fail.
        VerificationMode unrecoverableDelegate = mock(NoMoreInteractions.class);
        doThrow(new MockitoAssertionError("Unrecoverable failure"))
            .when(unrecoverableDelegate).verify(any(VerificationData.class));

        // The timeout is long, but the test should fail immediately without polling.
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, 5000L, unrecoverableDelegate, false);

        // Act & Assert
        try {
            verification.verify(mock(VerificationData.class));
            fail("Expected MockitoAssertionError for unrecoverable failure");
        } catch (MockitoAssertionError e) {
            assertEquals("Unrecoverable failure", e.getMessage());
        }
    }

    @Test
    public void verifyShouldThrowAssertionErrorAfterTimeoutWhenRecoverableDelegateNeverSucceeds() {
        // Arrange
        VerificationMode recoverableButFailingDelegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Condition not met"))
            .when(recoverableButFailingDelegate).verify(data);

        // This verification will poll for 100ms. We expect it to fail at the end.
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(10L, 100L, recoverableButFailingDelegate, false);

        // Act & Assert
        try {
            verification.verify(data);
            fail("Expected a MockitoAssertionError to be thrown after the timeout");
        } catch (MockitoAssertionError e) {
            // The final error thrown should be the last one caught from the delegate.
            assertEquals("Condition not met", e.getMessage());
        }
    }
}