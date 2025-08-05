package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Strictness;
import com.google.gson.ToNumberPolicy;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;

/**
 * Test suite for ToNumberPolicy enum to verify number parsing behavior
 * with various input formats and error conditions.
 */
public class ToNumberPolicyTest {

    @Test(timeout = 4000)
    public void testLongOrDoublePolicy_WithInvalidJsonString_ThrowsRuntimeException() throws Throwable {
        // Given: Invalid JSON string with malformed number format
        String invalidJson = "\"*.\"{/";
        JsonReader jsonReader = createJsonReader(invalidJson);
        ToNumberPolicy policy = ToNumberPolicy.LONG_OR_DOUBLE;
        
        // When & Then: Attempting to read number should throw RuntimeException
        try {
            policy.readNumber(jsonReader);
            fail("Expected RuntimeException for malformed JSON number");
        } catch (RuntimeException e) {
            assertTrue("Exception message should contain parsing error", 
                      e.getMessage().contains("Cannot parse *.;"));
        }
    }

    @Test(timeout = 4000)
    public void testBigDecimalPolicy_WithInvalidInput_InLenientMode_ThrowsRuntimeException() throws Throwable {
        // Given: Invalid input string in lenient parsing mode
        String invalidInput = "p(ppj%8[r\u0000>Md]";
        JsonReader jsonReader = createLenientJsonReader(invalidInput);
        ToNumberPolicy policy = ToNumberPolicy.BIG_DECIMAL;
        
        // When & Then: Should throw RuntimeException even in lenient mode
        try {
            policy.readNumber(jsonReader);
            fail("Expected RuntimeException for unparseable input");
        } catch (RuntimeException e) {
            assertTrue("Exception message should contain parsing error", 
                      e.getMessage().contains("Cannot parse p(ppj%8;"));
        }
    }

    @Test(timeout = 4000)
    public void testLongOrDoublePolicy_WithMalformedDecimal_InLenientMode_ThrowsRuntimeException() throws Throwable {
        // Given: Malformed decimal number in lenient parsing mode
        String malformedDecimal = ".a&;-m";
        JsonReader jsonReader = createLenientJsonReader(malformedDecimal);
        ToNumberPolicy policy = ToNumberPolicy.LONG_OR_DOUBLE;
        
        // When & Then: Should throw RuntimeException for malformed decimal
        try {
            policy.readNumber(jsonReader);
            fail("Expected RuntimeException for malformed decimal");
        } catch (RuntimeException e) {
            assertTrue("Exception message should contain parsing error", 
                      e.getMessage().contains("Cannot parse .a&;"));
        }
    }

    @Test(timeout = 4000)
    public void testLongOrDoublePolicy_WithGarbageInput_InLenientMode_ThrowsRuntimeException() throws Throwable {
        // Given: Garbage input that cannot be parsed as a number
        String garbageInput = "p(ppj%8[r\u0000>Md]";
        JsonReader jsonReader = createLenientJsonReader(garbageInput);
        ToNumberPolicy policy = ToNumberPolicy.LONG_OR_DOUBLE;
        
        // When & Then: Should throw RuntimeException for unparseable input
        try {
            policy.readNumber(jsonReader);
            fail("Expected RuntimeException for garbage input");
        } catch (RuntimeException e) {
            assertTrue("Exception message should contain parsing error", 
                      e.getMessage().contains("Cannot parse p(ppj%8;"));
        }
    }

    @Test(timeout = 4000)
    public void testLazilyParsedNumberPolicy_WithNullReader_ThrowsNullPointerException() throws Throwable {
        // Given: LAZILY_PARSED_NUMBER policy with null JsonReader
        ToNumberPolicy policy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
        
        // When & Then: Should throw NullPointerException for null reader
        try {
            policy.readNumber(null);
            fail("Expected NullPointerException for null JsonReader");
        } catch (NullPointerException e) {
            // Expected behavior - null reader should cause NPE
        }
    }

    @Test(timeout = 4000)
    public void testDoublePolicy_WithNullReader_ThrowsNullPointerException() throws Throwable {
        // Given: DOUBLE policy with null JsonReader
        ToNumberPolicy policy = ToNumberPolicy.DOUBLE;
        
        // When & Then: Should throw NullPointerException for null reader
        try {
            policy.readNumber(null);
            fail("Expected NullPointerException for null JsonReader");
        } catch (NullPointerException e) {
            // Expected behavior - null reader should cause NPE
        }
    }

    // Helper methods for better test readability
    
    private JsonReader createJsonReader(String input) {
        return new JsonReader(new StringReader(input));
    }
    
    private JsonReader createLenientJsonReader(String input) {
        JsonReader reader = new JsonReader(new StringReader(input));
        reader.setStrictness(Strictness.LENIENT);
        return reader;
    }
}