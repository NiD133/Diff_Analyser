package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CaseFormat#to(CaseFormat, String)} focusing on LOWER_UNDERSCORE to LOWER_CAMEL
 * conversions.
 */
@RunWith(JUnit4.class)
public class CaseFormatTest {

    @Test
    public void toLowerCamel_fromLowerUnderscoreWithSingleWord_isUnchanged() {
        // Arrange
        String singleWord = "foo";

        // Act
        String result = LOWER_UNDERSCORE.to(LOWER_CAMEL, singleWord);

        // Assert
        assertThat(result).isEqualTo("foo");
    }

    @Test
    public void toLowerCamel_fromLowerUnderscoreWithMultipleWords_convertsCorrectly() {
        // Arrange
        String lowerUnderscore = "foo_bar";
        String expectedLowerCamel = "fooBar";

        // Act
        String result = LOWER_UNDERSCORE.to(LOWER_CAMEL, lowerUnderscore);

        // Assert
        assertThat(result).isEqualTo(expectedLowerCamel);
    }
}