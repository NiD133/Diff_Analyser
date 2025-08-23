package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for conversions from {@link CaseFormat#LOWER_CAMEL} to {@link CaseFormat#UPPER_CAMEL}.
 */
public class CaseFormatTest extends TestCase {

  public void testLowerCamelToUpperCamel() {
    // Test case 1: A single word should just have its first letter capitalized.
    // Arrange
    String singleWordInput = "foo";
    String expectedSingleWordOutput = "Foo";

    // Act
    String actualOutput = LOWER_CAMEL.to(UPPER_CAMEL, singleWordInput);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedSingleWordOutput);

    // Test case 2: A standard multi-word lowerCamel string.
    // Arrange
    String multiWordInput = "fooBar";
    String expectedMultiWordOutput = "FooBar";

    // Act
    actualOutput = LOWER_CAMEL.to(UPPER_CAMEL, multiWordInput);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedMultiWordOutput);

    // Test case 3: An edge case with an acronym-like part.
    // The converter should treat "h" and "TTP" as separate words.
    // Arrange
    String acronymInput = "hTTP";
    String expectedAcronymOutput = "HTTP";

    // Act
    actualOutput = LOWER_CAMEL.to(UPPER_CAMEL, acronymInput);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedAcronymOutput);
  }
}