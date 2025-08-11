package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

/**
 * Test suite for CaseFormat enum functionality.
 * Tests conversion between different case formats like camelCase, snake_case, kebab-case, etc.
 */
public class CaseFormatTest {

    // ========== Basic Format Conversion Tests ==========
    
    @Test
    public void shouldConvertFromUpperUnderscoreToLowerCamel() {
        String result = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "HELLO_WORLD");
        assertEquals("helloWorld", result);
    }

    @Test
    public void shouldConvertFromLowerCamelToLowerUnderscore() {
        String result = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "helloWorld");
        assertEquals("hello_world", result);
    }

    @Test
    public void shouldConvertFromLowerUnderscoreToUpperCamel() {
        String result = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "hello_world");
        assertEquals("HelloWorld", result);
    }

    @Test
    public void shouldConvertFromLowerHyphenToUpperCamel() {
        String result = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, "hello-world");
        assertEquals("HelloWorld", result);
    }

    @Test
    public void shouldConvertFromLowerHyphenToLowerUnderscore() {
        String result = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, "hello-world");
        assertEquals("hello_world", result);
    }

    @Test
    public void shouldConvertFromLowerUnderscoreToUpperUnderscore() {
        String result = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, "hello_world");
        assertEquals("HELLO_WORLD", result);
    }

    // ========== Same Format Conversion Tests ==========
    
    @Test
    public void shouldReturnSameStringWhenConvertingToSameFormat() {
        String input = "HELLO_WORLD";
        String result = CaseFormat.UPPER_UNDERSCORE.convert(CaseFormat.UPPER_UNDERSCORE, input);
        assertEquals(input, result);
    }

    @Test
    public void shouldReturnSameStringWhenConvertingLowerCamelToItself() {
        String input = "helloWorld";
        String result = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, input);
        assertEquals(input, result);
    }

    @Test
    public void shouldReturnSameStringWhenConvertingLowerHyphenToItself() {
        String input = "hello-world";
        String result = CaseFormat.LOWER_HYPHEN.convert(CaseFormat.LOWER_HYPHEN, input);
        assertEquals(input, result);
    }

    @Test
    public void shouldReturnSameStringWhenConvertingLowerUnderscoreToItself() {
        String input = "hello_world";
        String result = CaseFormat.LOWER_UNDERSCORE.convert(CaseFormat.LOWER_UNDERSCORE, input);
        assertEquals(input, result);
    }

    // ========== Edge Cases Tests ==========
    
    @Test
    public void shouldHandleEmptyString() {
        String result = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, "");
        assertEquals("", result);
    }

    @Test
    public void shouldHandleEmptyStringInSameFormatConversion() {
        String result = CaseFormat.UPPER_CAMEL.convert(CaseFormat.UPPER_CAMEL, "");
        assertEquals("", result);
    }

    @Test
    public void shouldHandleEmptyStringInNormalizeFirstWord() {
        String result = CaseFormat.UPPER_CAMEL.normalizeFirstWord("");
        assertEquals("", result);
    }

    @Test
    public void shouldHandleSingleCharacterInNormalizeFirstWord() {
        String result = CaseFormat.UPPER_CAMEL.normalizeFirstWord("A");
        assertEquals("a", result);
    }

    // ========== Special Characters and Numbers Tests ==========
    
    @Test
    public void shouldHandleStringsWithSpecialCharactersAndNumbers() {
        // Testing with non-standard input that contains special characters
        String result = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "test123Special");
        assertEquals("test123_special", result);
    }

    @Test
    public void shouldPreserveSpecialCharactersInSameFormatConversion() {
        String input = "test@#$%";
        String result = CaseFormat.LOWER_UNDERSCORE.convert(CaseFormat.LOWER_UNDERSCORE, input);
        // The method applies toLowerCase, so uppercase letters become lowercase
        assertEquals("test@#$%", result);
    }

    // ========== Null Parameter Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenInputStringIsNull() {
        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenNormalizeFirstWordReceivesNull() {
        CaseFormat.LOWER_CAMEL.normalizeFirstWord(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenTargetFormatIsNull() {
        CaseFormat.LOWER_CAMEL.converterTo(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenConvertReceivesNullString() {
        CaseFormat.UPPER_UNDERSCORE.convert(CaseFormat.LOWER_CAMEL, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenConvertingFromLowerHyphenWithNullString() {
        CaseFormat.LOWER_HYPHEN.convert(CaseFormat.UPPER_UNDERSCORE, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenConvertingFromLowerUnderscoreWithNullString() {
        CaseFormat.LOWER_UNDERSCORE.convert(CaseFormat.LOWER_HYPHEN, null);
    }

    // ========== Enum Utility Tests ==========
    
    @Test
    public void shouldReturnAllCaseFormatValues() {
        CaseFormat[] formats = CaseFormat.values();
        assertEquals(5, formats.length);
    }

    @Test
    public void shouldReturnCorrectEnumValueFromString() {
        CaseFormat format = CaseFormat.valueOf("LOWER_CAMEL");
        assertEquals(CaseFormat.LOWER_CAMEL, format);
    }

    // ========== Converter Tests ==========
    
    @Test
    public void shouldCreateConverterBetweenSameFormats() {
        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);
        assertNotNull(converter);
    }

    @Test
    public void shouldCreateConverterBetweenDifferentFormats() {
        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);
        assertNotNull(converter);
        
        String result = converter.convert("helloWorld");
        assertEquals("HELLO_WORLD", result);
    }

    // ========== Case Handling Tests ==========
    
    @Test
    public void shouldHandleMixedCaseInput() {
        // Testing how the conversion handles mixed case input
        String result = CaseFormat.LOWER_CAMEL.convert(CaseFormat.LOWER_CAMEL, "UPPER_UNDERSCORE");
        // LOWER_CAMEL normalizeFirstWord converts first char to lowercase
        assertEquals("uPPER_UNDERSCORE", result);
    }

    @Test
    public void shouldConvertBetweenUnderscoreFormats() {
        String result = CaseFormat.UPPER_UNDERSCORE.convert(CaseFormat.LOWER_UNDERSCORE, "hello_world");
        assertEquals("hello_world", result);
    }

    @Test
    public void shouldHandleSingleHyphen() {
        String result = CaseFormat.UPPER_UNDERSCORE.convert(CaseFormat.LOWER_HYPHEN, "-");
        assertEquals("-", result);
    }
}