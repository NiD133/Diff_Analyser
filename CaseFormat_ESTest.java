package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.junit.Test;

/**
 * Understandable tests for {@link CaseFormat}.
 *
 * This test suite focuses on the public API, primarily the {@code to} and {@code converterTo}
 * methods, using clear and representative examples.
 */
public class CaseFormatTest {

    @Test
    public void shouldConvertFromLowerHyphenToOtherFormats() {
        assertEquals("test_string", CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, "test-string"));
        assertEquals("testString", CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-string"));
        assertEquals("TestString", CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, "test-string"));
        assertEquals("TEST_STRING", CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, "test-string"));
    }

    @Test
    public void shouldConvertFromLowerUnderscoreToOtherFormats() {
        assertEquals("test-string", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, "test_string"));
        assertEquals("testString", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_string"));
        assertEquals("TestString", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_string"));
        assertEquals("TEST_STRING", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, "test_string"));
    }

    @Test
    public void shouldConvertFromLowerCamelToOtherFormats() {
        assertEquals("test-string", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testString"));
        assertEquals("test_string", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testString"));
        assertEquals("TestString", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, "testString"));
        assertEquals("TEST_STRING", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "testString"));
    }

    @Test
    public void shouldConvertFromUpperCamelToOtherFormats() {
        assertEquals("test-string", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "TestString"));
        assertEquals("test_string", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "TestString"));
        assertEquals("testString", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, "TestString"));
        assertEquals("TEST_STRING", CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "TestString"));
    }

    @Test
    public void shouldConvertFromUpperUnderscoreToOtherFormats() {
        assertEquals("test-string", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, "TEST_STRING"));
        assertEquals("test_string", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, "TEST_STRING"));
        assertEquals("testString", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "TEST_STRING"));
        assertEquals("TestString", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "TEST_STRING"));
    }

    @Test
    public void to_withSameSourceAndTargetFormat_shouldReturnOriginalString() {
        String input = "TestString";
        // The 'to' method has a fast path that returns the original string if formats are the same.
        assertSame(input, CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_CAMEL, input));
    }

    @Test
    public void to_withEmptyString_shouldReturnEmptyString() {
        assertEquals("", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, ""));
    }

    @Test
    public void to_withLeadingAndTrailingSeparators_shouldHandleCorrectly() {
        // Leading and trailing separators result in empty first/last words, which are ignored.
        assertEquals("FooBar", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "_foo_bar_"));
        assertEquals("fooBar", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "_FOO_BAR_"));
    }

    @Test(expected = NullPointerException.class)
    public void to_withNullString_shouldThrowNullPointerException() {
        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, null);
    }

    @Test
    public void converterTo_shouldReturnAWorkingConverter() {
        // Arrange
        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);

        // Act & Assert
        assertEquals("ANOTHER_TEST_STRING", converter.convert("anotherTestString"));
        assertEquals("testString", converter.reverse().convert("TEST_STRING"));
    }

    @Test
    public void converterTo_identityConverter_shouldBehaveAsIdentityFunction() {
        // Arrange
        Converter<String, String> identityConverter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);
        String input = "someInput";

        // Act & Assert
        // The identity converter should return the exact same string instance.
        assertSame(input, identityConverter.convert(input));
    }

    @Test(expected = NullPointerException.class)
    public void converterTo_withNullTargetFormat_shouldThrowNullPointerException() {
        CaseFormat.LOWER_CAMEL.converterTo(null);
    }
}