/*
 * Test suite for LookupTranslator class
 * Tests the functionality of character sequence translation using lookup tables
 */

package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class LookupTranslator_ESTest extends LookupTranslator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testTranslateWithNoMatchingPattern_ReturnsOriginalString() throws Throwable {
        // Arrange: Create lookup table with null character buffer and "FFFFF15A" pattern
        CharSequence[] lookupEntry1 = createLookupEntry(createNullCharBuffer(), "FFFFF15A");
        CharSequence[] lookupEntry2 = createLookupEntry("FFFFF15A", "FFFFF15A");
        
        CharSequence[][] lookupTable = createLookupTable(lookupEntry1, lookupEntry2);
        LookupTranslator translator = new LookupTranslator(lookupTable);
        
        // Act: Translate a string that doesn't match any pattern
        String result = translator.translate("FFFFFFFF");
        
        // Assert: Should return original string when no pattern matches
        assertEquals("FFFFFFFF", result);
    }

    @Test(timeout = 4000)
    public void testConstructorWithIncompleteTable_ThrowsNullPointerException() throws Throwable {
        // Arrange: Create lookup table with incomplete entries (missing some array elements)
        CharSequence[] incompleteEntry1 = createLookupEntry(createNullCharBuffer(), "FFFFF15A");
        CharSequence[] incompleteEntry2 = createLookupEntry("FFFFF15A", "FFFFF15A");
        
        CharSequence[][] incompleteLookupTable = new CharSequence[8][3];
        incompleteLookupTable[0] = incompleteEntry1;
        incompleteLookupTable[1] = incompleteEntry2;
        // Remaining entries are null
        
        // Act & Assert: Constructor should throw NullPointerException for incomplete table
        try {
            new LookupTranslator(incompleteLookupTable);
            fail("Expected NullPointerException for incomplete lookup table");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateWithNullLookupTable_ReturnsZero() throws Throwable {
        // Arrange: Create translator with null lookup table
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        StringWriter output = new StringWriter();
        CharBuffer input = CharBuffer.wrap("557");
        
        // Act: Attempt translation with null lookup table
        int charactersTranslated = translator.translate(input, 0, output);
        
        // Assert: Should return 0 characters translated
        assertEquals(0, charactersTranslated);
    }

    @Test(timeout = 4000)
    public void testTranslateWithMatchingPattern_WritesReplacementAndReturnsLength() throws Throwable {
        // Arrange: Create lookup table with null char buffer mapping to "FFFFF15A"
        CharBuffer nullCharBuffer = createNullCharBuffer();
        CharSequence[] lookupEntry = createLookupEntry(nullCharBuffer, "FFFFF15A");
        CharSequence[][] lookupTable = new CharSequence[1][7];
        lookupTable[0] = lookupEntry;
        
        LookupTranslator translator = new LookupTranslator(lookupTable);
        StringWriter output = new StringWriter(1);
        
        // Act: Translate the null char buffer pattern
        int charactersTranslated = translator.translate(nullCharBuffer, 0, output);
        
        // Assert: Should write replacement string and return original pattern length
        assertEquals("FFFFF15A", output.toString());
        assertEquals(2, charactersTranslated); // Length of null char buffer
    }

    @Test(timeout = 4000)
    public void testTranslateWithInvalidIndex_ThrowsStringIndexOutOfBoundsException() throws Throwable {
        // Arrange: Create translator with empty lookup table
        LookupTranslator translator = new LookupTranslator(new CharSequence[0][3]);
        StringWriter output = new StringWriter();
        StringBuffer input = output.getBuffer();
        
        // Act & Assert: Should throw exception for invalid index
        try { 
            translator.translate(input, 2955, output); // Index way beyond string length
            fail("Expected StringIndexOutOfBoundsException for invalid index");
        } catch(StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testTranslateCharBufferWithInvalidIndex_ThrowsIndexOutOfBoundsException() throws Throwable {
        // Arrange: Create translator and CharBuffer with capacity 1
        LookupTranslator translator = new LookupTranslator(new CharSequence[0][9]);
        CharBuffer input = CharBuffer.allocate(1);
        StringWriter output = new StringWriter(1);
        
        // Act & Assert: Should throw exception when accessing beyond buffer capacity
        try { 
            translator.translate(input, 1, output); // Index beyond buffer capacity
            fail("Expected IndexOutOfBoundsException for CharBuffer index beyond capacity");
        } catch(IndexOutOfBoundsException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithEmptyStringBuffer_ThrowsStringIndexOutOfBoundsException() throws Throwable {
        // Arrange: Create lookup entry with empty StringBuffer
        StringWriter stringWriter = new StringWriter();
        StringBuffer emptyBuffer = stringWriter.getBuffer();
        CharSequence[] lookupEntry = {emptyBuffer, emptyBuffer};
        CharSequence[][] lookupTable = new CharSequence[1][9];
        lookupTable[0] = lookupEntry;
        
        // Act & Assert: Constructor should throw exception for empty string access
        try {
            new LookupTranslator(lookupTable);
            fail("Expected StringIndexOutOfBoundsException for empty StringBuffer");
        } catch(StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithEmptyCharBuffer_ThrowsIndexOutOfBoundsException() throws Throwable {
        // Arrange: Create lookup entry with empty CharBuffer
        char[] emptyCharArray = new char[0];
        CharBuffer emptyCharBuffer = CharBuffer.wrap(emptyCharArray);
        CharSequence[] lookupEntry = {emptyCharBuffer, emptyCharBuffer};
        CharSequence[][] lookupTable = new CharSequence[1][2];
        lookupTable[0] = lookupEntry;
        
        // Act & Assert: Constructor should throw exception for empty CharBuffer access
        try {
            new LookupTranslator(lookupTable);
            fail("Expected IndexOutOfBoundsException for empty CharBuffer");
        } catch(IndexOutOfBoundsException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithEmptyLookupEntry_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        // Arrange: Create lookup table with empty entry array
        CharSequence[][] lookupTable = new CharSequence[1][0]; // Empty entry
        
        // Act & Assert: Constructor should throw exception for empty lookup entry
        try {
            new LookupTranslator(lookupTable);
            fail("Expected ArrayIndexOutOfBoundsException for empty lookup entry");
        } catch(ArrayIndexOutOfBoundsException e) {
            assertEquals("0", e.getMessage());
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateWithMultipleLookupEntries_AppliesCorrectTranslations() throws Throwable {
        // Arrange: Create complex lookup table with multiple entries
        char[] charArray = {'\0', '1'}; // Null character followed by '1'
        CharBuffer sourceBuffer = CharBuffer.wrap(charArray);
        CharSequence[] lookupEntry1 = createLookupEntry(sourceBuffer, "FFFFF15A");
        
        // Create multiple lookup table entries
        CharSequence[][] lookupTable = new CharSequence[4][7];
        lookupTable[0] = lookupEntry1;
        lookupTable[1] = lookupEntry1;
        lookupTable[2] = lookupEntry1;
        
        // Create another entry with modified buffer
        sourceBuffer.get(); // Advance buffer position
        CharSequence[] lookupEntry2 = createLookupEntry(sourceBuffer, "FFFFF15A");
        lookupTable[3] = lookupEntry2;
        
        LookupTranslator translator = new LookupTranslator(lookupTable);
        CharBuffer inputBuffer = CharBuffer.wrap("FFFFF15A");
        
        // Act: Translate the input
        String result = translator.translate(inputBuffer);
        
        // Assert: Should apply translations based on lookup table
        assertEquals("FFFFFFFFFF15A5A", result);
    }

    @Test(timeout = 4000)
    public void testTranslateWithNullInput_ThrowsNullPointerException() throws Throwable {
        // Arrange: Create translator with null lookup table
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        StringWriter output = new StringWriter(7);
        
        // Act & Assert: Should throw exception for null input
        try { 
            translator.translate(null, 7, output);
            fail("Expected NullPointerException for null input");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }

    // Helper methods for better readability
    private CharBuffer createNullCharBuffer() {
        char[] charArray = new char[2]; // Creates array with null characters
        return CharBuffer.wrap(charArray);
    }
    
    private CharSequence[] createLookupEntry(CharSequence key, CharSequence value) {
        CharSequence[] entry = new CharSequence[3];
        entry[0] = key;
        entry[1] = value;
        return entry;
    }
    
    private CharSequence[][] createLookupTable(CharSequence[] entry1, CharSequence[] entry2) {
        CharSequence[][] table = new CharSequence[8][3];
        table[0] = entry1;
        table[1] = entry2;
        // Fill remaining slots with entry1 to avoid null entries
        for (int i = 2; i < 8; i++) {
            table[i] = entry1;
        }
        return table;
    }
}