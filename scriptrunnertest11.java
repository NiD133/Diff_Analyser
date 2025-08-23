package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This test class has been refactored for clarity from its original version,
 * which was named 'ScriptRunnerTestTest11'.
 *
 * The original test contained a single, poorly named method that did not test
 * the ScriptRunner class. Instead, it verified a fundamental behavior of Java's
 * StringBuilder: that a method modifying and returning a StringBuilder instance
 * returns a reference to the same object, not a copy.
 *
 * This refactored version clarifies the test's true purpose by:
 * 1. Renaming the class, methods, and variables to be descriptive.
 * 2. Removing all unused helper methods and imports related to ScriptRunner.
 *
 * While the test is now understandable, its placement within the 'org.apache.ibatis.jdbc'
 * package is questionable as it tests core Java behavior, not iBATIS functionality.
 */
@DisplayName("StringBuilder Modification Behavior Test")
class StringBuilderModificationBehaviorTest {

    /**
     * Modifies a StringBuilder by appending text and returns the same instance.
     *
     * @param builder The StringBuilder to modify.
     * @return The same StringBuilder instance that was passed as an argument.
     */
    private StringBuilder appendTextAndReturnSameInstance(StringBuilder builder) {
        builder.append("ABC");
        return builder;
    }

    @Test
    @DisplayName("Method should return the same StringBuilder instance after modification")
    void shouldReturnSameInstanceWhenModifyingAndReturningStringBuilder() {
        // Arrange: Create a new StringBuilder instance.
        StringBuilder originalBuilder = new StringBuilder();

        // Act: Pass the instance to a method that modifies and returns it.
        StringBuilder returnedBuilder = appendTextAndReturnSameInstance(originalBuilder);

        // Assert: Verify that the returned reference is the same as the original.
        // This confirms the expected pass-by-reference-value behavior for mutable objects.
        assertSame(originalBuilder, returnedBuilder,
            "The returned StringBuilder should be the same instance as the one passed in.");
    }
}