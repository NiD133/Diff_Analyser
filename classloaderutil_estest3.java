package org.apache.commons.jxpath.util;

import org.junit.Test;

/**
 * Test suite for {@link ClassLoaderUtil}.
 *
 * This class contains tests for the static methods of the ClassLoaderUtil class.
 */
// The original test class name "ClassLoaderUtil_ESTestTest3" was generated and not descriptive.
// Renaming it to "ClassLoaderUtilTest" follows standard Java testing conventions.
public class ClassLoaderUtilTest extends ClassLoaderUtil_ESTest_scaffolding {

    /**
     * Verifies that calling {@code ClassLoaderUtil.getClass()} with a name
     * for a class that does not exist results in a {@code ClassNotFoundException}.
     */
    // The original test name "test2" was uninformative. The new name clearly
    // describes the test's purpose: what it does and what it expects.
    @Test(timeout = 4000, expected = ClassNotFoundException.class)
    public void getClassWithNonExistentClassNameThrowsException() throws ClassNotFoundException {
        // Arrange: Define a class name that is syntactically valid but does not exist.
        // The original string "UQR~q#m;I=WyC" was unclear. This is more representative.
        final String nonExistentClassName = "org.apache.commons.jxpath.NonExistentClass";

        // Act & Assert: Attempt to load the non-existent class.
        // The @Test(expected=...) annotation handles the assertion, making the test
        // more concise and readable than the original try-catch-fail block.
        ClassLoaderUtil.getClass(nonExistentClassName, true);
    }
}