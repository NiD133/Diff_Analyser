package org.apache.commons.cli;

import org.junit.Test;

/**
 * Tests for the {@link OptionBuilder} class, focusing on exception-handling scenarios.
 */
public class OptionBuilderTest {

    /**
     * Tests that the deprecated {@code withType(Object)} method throws a
     * {@code ClassCastException} when passed an argument that is not an
     * instance of {@link java.lang.Class}. This confirms the behavior documented
     * for this legacy method, which performs an internal cast.
     */
    @Test(expected = ClassCastException.class)
    public void withType_whenArgumentIsNotAClass_shouldThrowClassCastException() {
        // The withType(Object) method is expected to cast its argument to a Class.
        // Providing a simple String should therefore cause a ClassCastException.
        OptionBuilder.withType("not a class object");
    }
}