package org.apache.commons.lang3.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import org.junit.Test;

/**
 * Tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    @Test
    public void getAccessibleConstructor_withNullConstructor_shouldThrowNullPointerException() {
        try {
            // The method under test is called with a null argument.
            ConstructorUtils.getAccessibleConstructor((Constructor<Object>) null);
            fail("Expected a NullPointerException to be thrown, but nothing was thrown.");
        } catch (final NullPointerException e) {
            // Verify that the exception has the expected message.
            // This is the message produced by Objects.requireNonNull(ctor, "ctor").
            assertEquals("ctor", e.getMessage());
        }
    }
}