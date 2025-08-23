package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TypeSafeMatchingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void shouldReturnTrue_whenNullMatcherIsAppliedToNullArgument() {
        // The main purpose of TypeSafeMatching is to prevent ClassCastExceptions.
        // This test verifies that applying a null-checking matcher to a null argument
        // is handled safely and correctly returns true.

        // Arrange
        ArgumentMatcher<?> nullMatcher = Null.NULL;
        Object nullArgument = null;

        // Act
        boolean isMatch = matchesTypeSafe().apply(nullMatcher, nullArgument);

        // Assert
        assertThat(isMatch)
            .as("Applying a null-checking matcher to a null argument should result in a match")
            .isTrue();
    }
}