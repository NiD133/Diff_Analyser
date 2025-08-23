package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest extends TestCase {

    public void testUpperCamelToLowerHyphen() {
        // Arrange: A single-word string in UpperCamel format.
        String singleWordInput = "Foo";
        String expectedSingleWordOutput = "foo";

        // Act & Assert: Verify the conversion to lower-hyphen.
        assertThat(UPPER_CAMEL.to(LOWER_HYPHEN, singleWordInput))
            .isEqualTo(expectedSingleWordOutput);

        // Arrange: A multi-word string in UpperCamel format.
        String multiWordInput = "FooBar";
        String expectedMultiWordOutput = "foo-bar";

        // Act & Assert: Verify the conversion to lower-hyphen.
        assertThat(UPPER_CAMEL.to(LOWER_HYPHEN, multiWordInput))
            .isEqualTo(expectedMultiWordOutput);
    }
}