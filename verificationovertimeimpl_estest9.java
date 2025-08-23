package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.verification.VerificationMode;

import java.util.NoSuchElementException;

/**
 * Test suite for {@link VerificationOverTimeImpl}.
 * Note: This class contains a refactored version of a test originally found in a generated suite.
 */
public class VerificationOverTimeImplTest {

    /**
     * Verifies that copyWithVerificationMode throws an exception when the new mode is another
     * VerificationOverTimeImpl instance.
     *
     * <p>This test covers a specific, and potentially unexpected, behavior. The expectation of a
     * {@code NoSuchElementException} suggests this test acts as a regression test for a previously
     * discovered issue or an edge case, possibly related to an internal data structure being
     * in an invalid state during the copy operation.
     */
    @Test(expected = NoSuchElementException.class)
    public void copyWithVerificationMode_shouldThrowException_whenNewModeIsAlsoAnInstanceOfVerificationOverTime() {
        // Arrange
        // Create an initial instance of VerificationOverTimeImpl. The specific constructor
        // arguments are not critical, as the exception is triggered by the type of the
        // argument passed to the copy method.
        VerificationMode initialDelegate = null;
        VerificationOverTimeImpl originalMode = new VerificationOverTimeImpl(100L, 100L, initialDelegate, true);

        // Create a second instance by copying the first, mirroring the original test's setup.
        VerificationOverTimeImpl modeToModify = originalMode.copyWithVerificationMode(initialDelegate);

        // Act & Assert
        // The call to copyWithVerificationMode is expected to throw NoSuchElementException
        // when passed another instance of its own class. The assertion is handled by the
        // @Test(expected=...) annotation.
        modeToModify.copyWithVerificationMode(originalMode);
    }
}