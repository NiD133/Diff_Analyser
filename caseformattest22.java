package com.google.common.base;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat} conversions from {@code UPPER_CAMEL} to {@code UPPER_UNDERSCORE}.
 */
public class CaseFormatTest extends TestCase {

    public void testUpperCamelToUpperUnderscore_singleWord() {
        // Arrange
        String input = "Foo";
        String expected = "FOO";

        // Act
        String result = UPPER_CAMEL.to(UPPER_UNDERSCORE, input);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    public void testUpperCamelToUpperUnderscore_multipleWords() {
        // Arrange
        String input = "FooBar";
        String expected = "FOO_BAR";

        // Act
        String result = UPPER_CAMEL.to(UPPER_UNDERSCORE, input);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    public void testUpperCamelToUpperUnderscore_acronym() {
        // In UPPER_CAMEL, a word boundary is detected before each uppercase letter.
        // Therefore, an acronym like "HTTP" is treated as four separate words: "H", "T", "T", "P".
        // Arrange
        String input = "HTTP";
        String expected = "H_T_T_P";

        // Act
        String result = UPPER_CAMEL.to(UPPER_UNDERSCORE, input);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    public void testUpperCamelToUpperUnderscore_inputAlreadyContainingSeparators() {
        // The conversion logic does not treat existing underscores as special.
        // "H_T_T_P" is parsed as words "H", "_T", "_T", "_P", and then converted,
        // which results in double underscores in the output.
        // Arrange
        String input = "H_T_T_P";
        String expected = "H__T__T__P";

        // Act
        String result = UPPER_CAMEL.to(UPPER_UNDERSCORE, input);

        // Assert
        assertThat(result).isEqualTo(expected);
    }
}