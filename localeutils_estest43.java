package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests the public, deprecated constructor.
     * <p>
     * The {@link LocaleUtils} class is a utility class with static methods and is not
     * intended to be instantiated. However, it has a public constructor for compatibility
     * with tools that require a JavaBean instance.
     * </p>
     * <p>
     * This test confirms that the constructor can be called without throwing an exception,
     * primarily for code coverage purposes.
     * </p>
     */
    @Test
    public void testConstructor() {
        // Create an instance to ensure the public constructor is covered.
        final LocaleUtils instance = new LocaleUtils();
        
        // Assert that the instance is created successfully.
        assertNotNull("A new instance should have been created.", instance);
    }
}