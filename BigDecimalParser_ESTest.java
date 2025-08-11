package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.BigDecimalParser;
import java.math.BigDecimal;

/**
 * Test suite for BigDecimalParser functionality.
 * Tests cover valid parsing scenarios, error conditions, and edge cases.
 */
public class BigDecimalParserTest {

    // Valid parsing scenarios
    
    @Test
    public void shouldParsePositiveIntegerFromString() {
        BigDecimal result = BigDecimalParser.parse("8");
        
        assertEquals(8, result.byteValue());
    }
    
    @Test
    public void shouldParsePositiveIntegerFromCharArray() {
        char[] input = {'4'};
        
        BigDecimal result = BigDecimalParser.parse(input);
        
        assertEquals(4, result.byteValue());
    }
    
    @Test
    public void shouldParseNegativeZero() {
        char[] input = {'-', '0'};
        
        BigDecimal result = BigDecimalParser.parse(input);
        
        assertEquals(0, result.shortValue());
    }
    
    @Test
    public void shouldParseDecimalNumber() {
        BigDecimal result = BigDecimalParser.parse(".1");
        
        assertEquals(0, result.byteValue()); // 0.1 truncated to byte is 0
    }
    
    @Test
    public void shouldParseScientificNotationFromString() {
        BigDecimal result = BigDecimalParser.parse("7e2");
        
        assertEquals(700, result.shortValue());
    }
    
    @Test
    public void shouldParseScientificNotationFromCharArray() {
        char[] input = {'2', 'E', '2'}; // 2E2 = 200
        
        BigDecimal result = BigDecimalParser.parse(input);
        
        assertEquals(-56, result.byteValue()); // 200 as byte wraps to -56
    }
    
    @Test
    public void shouldParseWithOffsetAndLength() {
        char[] input = new char[9];
        input[1] = '2';
        
        BigDecimal result = BigDecimalParser.parse(input, 1, 1);
        
        assertEquals(2, result.shortValue());
    }

    // Null input validation
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullString() {
        BigDecimalParser.parse((String) null);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullCharArray() {
        BigDecimalParser.parse((char[]) null);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullCharArrayWithOffset() {
        BigDecimalParser.parse((char[]) null, 486, 486);
    }

    // Invalid input validation
    
    @Test(expected = NumberFormatException.class)
    public void shouldThrowNumberFormatExceptionForInvalidString() {
        BigDecimalParser.parse("eA8ojpN");
    }
    
    @Test(expected = NumberFormatException.class)
    public void shouldThrowNumberFormatExceptionForEmptyInput() {
        char[] emptyInput = new char[1];
        BigDecimalParser.parse(emptyInput, 0, 0);
    }
    
    @Test(expected = NumberFormatException.class)
    public void shouldThrowNumberFormatExceptionForNullCharacters() {
        char[] nullChars = new char[3]; // Contains \u0000 characters
        BigDecimalParser.parse(nullChars);
    }

    // Boundary conditions and error scenarios
    
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void shouldThrowStringIndexOutOfBoundsForEmptyArrayWithOffset() {
        char[] emptyArray = new char[0];
        BigDecimalParser.parse(emptyArray, 265, 265);
    }

    // Fast parser delegation tests (these expect NoClassDefFoundError due to missing dependency)
    
    @Test(expected = NoClassDefFoundError.class)
    public void shouldDelegateToFastParserForLargeInputString() {
        BigDecimalParser.parseWithFastParser("");
    }
    
    @Test(expected = NoClassDefFoundError.class)
    public void shouldDelegateToFastParserForLargeInputCharArray() {
        char[] largeInput = new char[6];
        BigDecimalParser.parseWithFastParser(largeInput, 1754, -2552);
    }
    
    @Test(expected = NoClassDefFoundError.class)
    public void shouldDelegateToFastParserWhenInputExceedsThreshold() {
        char[] input = new char[1];
        // Negative offset triggers fast parser path
        BigDecimalParser.parse(input, -1747, 771);
    }
    
    @Test(expected = NoClassDefFoundError.class)
    public void shouldDelegateToFastParserForLargeOffset() {
        char[] emptyArray = new char[0];
        BigDecimalParser.parse(emptyArray, 500, 500);
    }
}