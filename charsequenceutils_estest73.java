package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests the public, deprecated constructor.
     *
     * <p>The {@link CharSequenceUtils} class is a utility class not meant for instantiation.
     * However, its constructor is public to support tools that require a JavaBean instance.
     * This test ensures the constructor can be called without errors, fulfilling this
     * compatibility requirement.
     * </p>
     */
    @Test
    public void testConstructor_shouldCreateInstanceForJavaBeanCompatibility() {
        // Instantiate the class to cover the public constructor.
        final CharSequenceUtils instance = new CharSequenceUtils();

        // Assert that an instance was successfully created.
        assertNotNull("The instance should not be null.", instance);
    }
}