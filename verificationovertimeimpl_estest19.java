package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * Verifies that the isReturnOnSuccess() method correctly returns false
     * when the object is initialized with the returnOnSuccess flag set to false.
     */
    @Test
    public void isReturnOnSuccessShouldReturnFalseWhenConstructedWithFalse() {
        // Arrange: Create an instance with the 'returnOnSuccess' parameter set to false.
        // The other constructor arguments (polling period, duration, delegate mode) are not
        // relevant for this specific test and can be set to default/null values.
        boolean shouldReturnOnSuccess = false;
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(
                0L, 0L, null, shouldReturnOnSuccess);

        // Act: Call the getter method under test.
        boolean actualValue = verification.isReturnOnSuccess();

        // Assert: The returned value should match the value provided during construction.
        assertFalse("Expected isReturnOnSuccess() to be false", actualValue);
    }
}