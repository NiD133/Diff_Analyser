package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link CookieUtil} class.
 */
public class CookieUtilTest {

    /**
     * Verifies that the CookieUtil utility class can be instantiated successfully.
     * <p>
     * Although {@link CookieUtil} is a utility class with static methods, this test
     * ensures the default constructor is callable and provides basic code coverage.
     * </p>
     */
    @Test
    public void instantiationSucceeds() {
        // Act: Attempt to create an instance of the utility class.
        CookieUtil cookieUtil = new CookieUtil();

        // Assert: The created instance should not be null.
        assertNotNull("A new CookieUtil instance should be successfully created.", cookieUtil);
    }
}