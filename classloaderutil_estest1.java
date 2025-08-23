package org.apache.commons.jxpath.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link ClassLoaderUtil}.
 */
public class ClassLoaderUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that getClass() throws a NullPointerException when the class name is null.
     */
    @Test
    public void getClass_withNullClassName_throwsNullPointerException() {
        // Arrange: We expect a NullPointerException with a specific message.
        // This is a standard check, often implemented with Objects.requireNonNull(className, "className").
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("className");

        // Act: Call the method under test with a null class name.
        ClassLoaderUtil.getClass(null, false);
    }
}