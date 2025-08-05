package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for NumericEntityUnescaper class.
 * Tests the conversion of HTML/XML numeric entities (e.g., &#65;, &#x41;) back to characters.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class NumericEntityUnescaper_ESTest extends NumericEntityUnescaper_ESTest_scaffolding {

    private static final NumericEntityUnescaper.OPTION[] NO_OPTIONS = new NumericEntityUnescaper.OPTION[0];

    // ========== Valid Entity Translation Tests ==========

    @Test(timeout = 4000)
    public void shouldTranslateDecimalNumericEntity() throws Throwable {
        // Given: A decimal numeric entity &#3; (should convert to character with code 3)
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = {'\0', '\0', '\0', '&', '#', '3', ';'};
        CharBuffer charBuffer = CharBuffer.wrap(input);
        StringWriter output = new StringWriter();
        
        // When: Translating at the position of the entity
        int charactersConsumed = unescaper.translate(charBuffer, 3, output);
        
        // Then: Should convert &#3; to character with code 3 and consume 4 characters
        assertEquals("Entity should be converted to character with code 3", "\u0003", output.toString());
        assertEquals("Should consume 4 characters (&#3;)", 4, charactersConsumed);
    }

    // ========== Invalid/Incomplete Entity Tests ==========

    @Test(timeout = 4000)
    public void shouldNotTranslateIncompleteEntityWithNonDigit() throws Throwable {
        // Given: An incomplete entity &#y (not a valid numeric entity)
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = {'\0', '&', '#', 'y', '\0', '\0', '\0', '\0'};
        CharBuffer charBuffer = CharBuffer.wrap(input);
        StringWriter output = new StringWriter();
        
        // When: Translating the entire sequence
        unescaper.translate(charBuffer, output);
        
        // Then: Should output the original text unchanged
        assertEquals("Invalid entity should remain unchanged", "\u0000&#y\u0000\u0000\u0000\u0000", output.toString());
    }

    @Test(timeout = 4000)
    public void shouldNotTranslateHexEntityWithoutDigits() throws Throwable {
        // Given: A hex entity marker without valid hex digits (&#x;)
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = {'\0', '\0', '&', '#', 'x', ';'};
        CharBuffer charBuffer = CharBuffer.wrap(input);
        
        // When: Translating the sequence
        String result = unescaper.translate(charBuffer);
        
        // Then: Should remain unchanged as it's not a valid entity
        assertEquals("Invalid hex entity should remain unchanged", "\u0000\u0000&#x;", result);
    }

    @Test(timeout = 4000)
    public void shouldNotTranslateIncompleteDecimalEntity() throws Throwable {
        // Given: An incomplete decimal entity without semicolon at end (&#5)
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = {'\0', '\0', '\0', '\0', '\0', '&', '#', '5'};
        CharBuffer charBuffer = CharBuffer.wrap(input);
        
        // When: Translating the sequence
        String result = unescaper.translate(charBuffer);
        
        // Then: Should remain unchanged as semicolon is required by default
        assertEquals("Incomplete entity should remain unchanged", "\u0000\u0000\u0000\u0000\u0000&#5", result);
    }

    @Test(timeout = 4000)
    public void shouldNotTranslateIncompleteHexEntity() throws Throwable {
        // Given: An incomplete hex entity without semicolon (&#x)
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = {'\0', '\0', '\0', '\0', '&', '#', 'x', '\0'};
        CharBuffer charBuffer = CharBuffer.wrap(input);
        
        // When: Translating the sequence
        String result = unescaper.translate(charBuffer);
        
        // Then: Should remain unchanged
        assertEquals("Incomplete hex entity should remain unchanged", "\u0000\u0000\u0000\u0000&#x\u0000", result);
    }

    @Test(timeout = 4000)
    public void shouldNotTranslateUppercaseHexMarker() throws Throwable {
        // Given: A hex entity with uppercase X marker (&#X)
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = {'\0', '\0', '\0', '\0', '\0', '&', '#', 'X'};
        CharBuffer charBuffer = CharBuffer.wrap(input);
        
        // When: Translating the sequence
        String result = unescaper.translate(charBuffer);
        
        // Then: Should remain unchanged (only lowercase 'x' is supported)
        assertEquals("Uppercase hex marker should not be recognized", "\u0000\u0000\u0000\u0000\u0000&#X", result);
    }

    // ========== Non-Entity Text Tests ==========

    @Test(timeout = 4000)
    public void shouldPassThroughRegularTextUnchanged() throws Throwable {
        // Given: Regular text without any entities
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        String input = "D+RTgb,eb:&],ms";
        StringWriter output = new StringWriter();
        
        // When: Translating the text
        unescaper.translate(input, output);
        
        // Then: Should remain exactly the same
        assertEquals("Regular text should pass through unchanged", "D+RTgb,eb:&],ms", output.toString());
    }

    @Test(timeout = 4000)
    public void shouldPassThroughTextWithIsolatedAmpersand() throws Throwable {
        // Given: Text with isolated ampersand (not part of entity)
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        String input = "D+RTgb,eb:&s";
        StringWriter output = new StringWriter();
        
        // When: Translating the text
        unescaper.translate(input, output);
        
        // Then: Should remain unchanged
        assertEquals("Text with isolated ampersand should remain unchanged", "D+RTgb,eb:&s", output.toString());
    }

    // ========== Edge Case and Boundary Tests ==========

    @Test(timeout = 4000)
    public void shouldReturnZeroForNonEntityCharacter() throws Throwable {
        // Given: A single character that's not an entity
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        
        // When: Trying to translate at position 0 of "0"
        int result = unescaper.translate("0", 0, null);
        
        // Then: Should return 0 (no characters consumed)
        assertEquals("Non-entity character should consume 0 characters", 0, result);
    }

    @Test(timeout = 4000)
    public void shouldHandleBufferWithOnlyAmpersand() throws Throwable {
        // Given: A buffer with ampersand at the end
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = {'\0', '&'};
        CharBuffer charBuffer = CharBuffer.wrap(input);
        StringWriter output = new StringWriter();
        
        // When: Translating the buffer
        unescaper.translate(charBuffer, output);
        
        // Then: Buffer position should remain at start (no valid entity found)
        assertEquals("Buffer position should remain at start", 0, charBuffer.position());
    }

    @Test(timeout = 4000)
    public void shouldHandleBufferWithOnlyAmpersandAtStart() throws Throwable {
        // Given: A buffer starting with ampersand but no valid entity
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        char[] input = new char[6];
        input[0] = '&';
        CharBuffer charBuffer = CharBuffer.wrap(input);
        StringWriter output = new StringWriter();
        
        // When: Translating the buffer
        unescaper.translate(charBuffer, output);
        
        // Then: Should complete without error
        assertTrue("Buffer should still have array backing", charBuffer.hasArray());
    }

    // ========== Exception Tests ==========

    @Test(timeout = 4000)
    public void shouldThrowExceptionForIndexOutOfBounds() throws Throwable {
        // Given: An unescaper and a buffer with invalid index
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        StringWriter stringWriter = new StringWriter(1416);
        StringBuffer stringBuffer = stringWriter.getBuffer();
        
        // When/Then: Accessing beyond buffer bounds should throw exception
        try {
            unescaper.translate(stringBuffer, 1416, stringWriter);
            fail("Should throw StringIndexOutOfBoundsException for invalid index");
        } catch(StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForNullInput() throws Throwable {
        // Given: An unescaper with null input
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        StringWriter output = new StringWriter();
        
        // When/Then: Null input should throw NullPointerException
        try {
            unescaper.translate(null, Integer.MAX_VALUE, output);
            fail("Should throw NullPointerException for null input");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForCharBufferIndexOutOfBounds() throws Throwable {
        // Given: A CharBuffer and invalid index
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NO_OPTIONS);
        CharBuffer charBuffer = CharBuffer.allocate(2261);
        StringWriter output = new StringWriter();
        
        // When/Then: Invalid index should throw IndexOutOfBoundsException
        try {
            unescaper.translate(charBuffer, 2261, output);
            fail("Should throw IndexOutOfBoundsException for invalid CharBuffer index");
        } catch(IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForNullOptionInArray() throws Throwable {
        // Given: An options array containing null
        NumericEntityUnescaper.OPTION[] optionsWithNull = new NumericEntityUnescaper.OPTION[1];
        // optionsWithNull[0] is null by default
        
        // When/Then: Null option should cause NullPointerException during construction
        try {
            new NumericEntityUnescaper(optionsWithNull);
            fail("Should throw NullPointerException for null option in array");
        } catch(NullPointerException e) {
            // Expected exception - EnumSet cannot handle null values
        }
    }
}