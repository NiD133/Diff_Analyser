package org.apache.commons.cli;

import org.junit.Test;

/**
 * This test suite focuses on the TypeHandler class.
 * The original test was auto-generated, making it difficult to understand.
 * This version has been refactored for clarity and maintainability.
 */
public class TypeHandler_ESTestTest9 extends TypeHandler_ESTest_scaffolding {

    /**
     * Tests that the deprecated {@code createValue(String, Object)} method throws a
     * {@code ClassCastException} if the second argument, which is supposed to represent
     * a type, is not a {@code Class} instance.
     *
     * The method under test internally casts its second argument to a {@code Class},
     * which will fail when a different type, such as a {@code String}, is provided.
     */
    @Test(expected = ClassCastException.class)
    public void createValueWithObjectShouldThrowClassCastExceptionWhenTypeObjectIsNotAClass() throws ParseException {
        // The first argument (the value string) is irrelevant for this test.
        // The second argument is a String instance, but the method expects a Class instance,
        // which should trigger a ClassCastException.
        TypeHandler.createValue("", "");
    }
}