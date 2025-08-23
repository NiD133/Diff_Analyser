package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#ensureNotNull(Object, String, Object...)} throws a
     * {@code NullPointerException} when the object to validate is null and the provided
     * error message string is also null.
     * <p>
     * This is an edge case. The {@code NullPointerException} is expected because the
     * underlying implementation attempts to format the null message string via
     * {@code String.format(null, ...)}, which is not permitted.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void ensureNotNullWithNullObjectAndNullMessageThrowsNPE() {
        // Act: Call ensureNotNull with a null object and a null message format.
        Validate.ensureNotNull(null, null);
    }
}