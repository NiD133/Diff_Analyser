package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests the public, deprecated constructor.
     *
     * <p>The constructor is not intended for general use, as all methods in CharSetUtils are static.
     * However, it is public to allow instantiation by tools that require a JavaBean instance.
     * This test confirms that the class can be instantiated, primarily for code coverage purposes
     * and to ensure JavaBean compatibility is maintained until the constructor is made private.</p>
     */
    @Test
    public void testConstructorForJavaBeanCompatibility() {
        // The CharSetUtils class has a public constructor for tools that require a
        // JavaBean instance, even though all its methods are static.
        final CharSetUtils instance = new CharSetUtils();

        // Assert that the instance is created successfully. This confirms that the
        // constructor works as expected for tools that might rely on it.
        assertNotNull("The instance should not be null.", instance);
    }
}