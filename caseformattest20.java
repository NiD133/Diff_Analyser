package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat} conversion from UPPER_CAMEL to LOWER_CAMEL.
 */
public class CaseFormatTest extends TestCase {

    public void testUpperCamelToLowerCamel_withSingleWord() {
        // Arrange
        String input = "Foo";
        String expectedOutput = "foo";

        // Act
        String result = UPPER_CAMEL.to(LOWER_CAMEL, input);

        // Assert
        assertThat(result).isEqualTo(expectedOutput);
    }

    public void testUpperCamelToLowerCamel_withMultipleWords() {
        // Arrange
        String input = "FooBar";
        String expectedOutput = "fooBar";

        // Act
        String result = UPPER_CAMEL.to(LOWER_CAMEL, input);

        // Assert
        assertThat(result).isEqualTo(expectedOutput);
    }

    public void testUpperCamelToLowerCamel_withAcronymLikeString() {
        // Arrange
        String input = "HTTP";
        String expectedOutput = "hTTP";

        // This behavior is by design. The UPPER_CAMEL format does not define a word
        // boundary between consecutive capital letters. Therefore, "HTTP" is treated
        // as a single word, and the conversion to LOWER_CAMEL only lowercases the
        // first character of the entire string.
        
        // Act
        String result = UPPER_CAMEL.to(LOWER_CAMEL, input);

        // Assert
        assertThat(result).isEqualTo(expectedOutput);
    }
}