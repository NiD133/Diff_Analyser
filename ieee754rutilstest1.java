package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest extends AbstractLangTest {

    /**
     * Tests the public constructor, which is deprecated and scheduled to be made private in a future release.
     * This test exists for code coverage and to ensure the constructor remains callable without error until it is removed.
     *
     * @see IEEE754rUtils#IEEE754rUtils()
     */
    @Test
    void testConstructor() {
        // A utility class with a public constructor should be constructable without exceptions.
        assertDoesNotThrow(IEEE754rUtils::new, "The constructor should not throw an exception.");
    }
}