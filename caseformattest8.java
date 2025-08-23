package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat#to(CaseFormat, String)} focusing on the conversion
 * from LOWER_UNDERSCORE to LOWER_HYPHEN.
 */
// The original class name `CaseFormatTestTest8` is kept, but a more standard name
// would be `CaseFormatTest`.
public class CaseFormatTestTest8 extends TestCase {

  public void testTo_fromLowerUnderscoreToLowerHyphen_singleWord() {
    // Arrange
    String singleWord = "foo";

    // Act
    String result = LOWER_UNDERSCORE.to(LOWER_HYPHEN, singleWord);

    // Assert
    // A single word with no delimiters should remain unchanged.
    assertThat(result).isEqualTo("foo");
  }

  public void testTo_fromLowerUnderscoreToLowerHyphen_multipleWords() {
    // Arrange
    String lowerUnderscoreInput = "foo_bar";
    String expectedLowerHyphenOutput = "foo-bar";

    // Act
    String result = LOWER_UNDERSCORE.to(LOWER_HYPHEN, lowerUnderscoreInput);

    // Assert
    // Underscores should be converted to hyphens.
    assertThat(result).isEqualTo(expectedLowerHyphenOutput);
  }
}