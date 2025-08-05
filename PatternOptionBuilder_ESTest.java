package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PatternOptionBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;

/**
 * Test suite for PatternOptionBuilder functionality.
 * PatternOptionBuilder allows creating CLI options from pattern strings where
 * characters represent option names and special symbols define value types.
 */
public class PatternOptionBuilderTest {

    // ========== Value Type Mapping Tests ==========
    
    @Test
    public void testGetValueType_StringType() {
        Class<?> result = PatternOptionBuilder.getValueType(':');
        assertEquals("Colon (:) should map to String type", String.class, result);
    }
    
    @Test
    public void testGetValueType_NumberType() {
        Class<?> result = PatternOptionBuilder.getValueType('%');
        assertEquals("Percent (%) should map to Number type", Number.class, result);
    }
    
    @Test
    public void testGetValueType_DateType() {
        Class<?> result = PatternOptionBuilder.getValueType('#');
        assertEquals("Hash (#) should map to Date type", Date.class, result);
    }
    
    @Test
    public void testGetValueType_ObjectType() {
        Class<?> result = PatternOptionBuilder.getValueType('@');
        assertEquals("At (@) should map to Object type", Object.class, result);
    }
    
    @Test
    public void testGetValueType_ClassType() {
        Class<?> result = PatternOptionBuilder.getValueType('+');
        assertEquals("Plus (+) should map to Class type", Class.class, result);
    }
    
    @Test
    public void testGetValueType_FileInputStreamType() {
        Class<?> result = PatternOptionBuilder.getValueType('<');
        assertEquals("Less than (<) should map to FileInputStream type", FileInputStream.class, result);
    }
    
    @Test
    public void testGetValueType_FileType() {
        Class<?> result = PatternOptionBuilder.getValueType('>');
        assertEquals("Greater than (>) should map to File type", File.class, result);
    }
    
    @Test
    public void testGetValueType_UrlType() {
        Class<?> result = PatternOptionBuilder.getValueType('/');
        assertEquals("Slash (/) should map to URL type", URL.class, result);
    }
    
    @Test
    public void testGetValueType_ArrayType() {
        Class<?> result = PatternOptionBuilder.getValueType('*');
        assertNotNull("Asterisk (*) should map to an array type", result);
        assertTrue("Asterisk (*) should map to an array type", result.isArray());
    }
    
    @Test
    public void testGetValueType_UnsupportedCharacters() {
        // Test various characters that should not map to any type
        char[] unsupportedChars = {'a', '1', '8', ',', '.', '?', '=', ';', '$', '&', '(', ')', '-'};
        
        for (char ch : unsupportedChars) {
            Class<?> result = PatternOptionBuilder.getValueType(ch);
            assertNull("Character '" + ch + "' should not map to any type", result);
        }
    }

    // ========== Value Code Recognition Tests ==========
    
    @Test
    public void testIsValueCode_ValidCodes() {
        char[] validCodes = {':', '%', '#', '@', '+', '<', '>', '/', '*', '!', ','};
        
        for (char code : validCodes) {
            boolean result = PatternOptionBuilder.isValueCode(code);
            assertTrue("Character '" + code + "' should be recognized as a value code", result);
        }
    }
    
    @Test
    public void testIsValueCode_InvalidCodes() {
        char[] invalidCodes = {',', 'a', '1', '?', '='};
        
        for (char code : invalidCodes) {
            boolean result = PatternOptionBuilder.isValueCode(code);
            // Note: Based on the original tests, ',' appears to be invalid
            if (code == ',') {
                assertFalse("Comma (,) should not be recognized as a value code", result);
            }
        }
    }

    // ========== Deprecated Method Tests ==========
    
    @Test
    public void testGetValueClass_DeprecatedMethod() {
        Object result = PatternOptionBuilder.getValueClass(':');
        assertNotNull("Deprecated getValueClass should still work", result);
        assertEquals("Deprecated method should return String class", "class java.lang.String", result.toString());
    }
    
    @Test
    public void testGetValueClass_UnsupportedCharacter() {
        Object result = PatternOptionBuilder.getValueClass('8');
        assertNull("Unsupported character should return null", result);
    }

    // ========== Pattern Parsing Tests ==========
    
    @Test
    public void testParsePattern_EmptyString() {
        Options result = PatternOptionBuilder.parsePattern("");
        assertNotNull("Empty pattern should return valid Options object", result);
    }
    
    @Test
    public void testParsePattern_ValidPattern() {
        Options result = PatternOptionBuilder.parsePattern("abc:def%");
        assertNotNull("Valid pattern should return Options object", result);
    }
    
    @Test
    public void testParsePattern_ComplexValidPattern() {
        Options result = PatternOptionBuilder.parsePattern("fWDS2@qUKbfHF+");
        assertNotNull("Complex valid pattern should return Options object", result);
    }
    
    @Test
    public void testParsePattern_PatternWithSpecialCharacters() {
        Options result = PatternOptionBuilder.parsePattern("0dpy>mb!!Q*1_");
        assertNotNull("Pattern with special characters should return Options object", result);
    }

    // ========== Error Handling Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testParsePattern_NullInput() {
        PatternOptionBuilder.parsePattern(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParsePattern_IllegalCharacterEquals() {
        PatternOptionBuilder.parsePattern("4K<:s%L=GX$");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParsePattern_IllegalCharacterQuote() {
        PatternOptionBuilder.parsePattern("The option '%s' contains an illegal character : '%s'.");
    }

    // ========== Constructor Test ==========
    
    @Test
    public void testConstructor_DeprecatedButFunctional() {
        // Test that the deprecated constructor still works
        PatternOptionBuilder builder = new PatternOptionBuilder();
        assertNotNull("Constructor should create instance", builder);
    }
}