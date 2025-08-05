/*
 * Test suite for DefaultSplitCharacter class
 * Tests the functionality of determining split characters in PDF text processing
 */

package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.DefaultSplitCharacter;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfChunk;
import com.itextpdf.text.pdf.PdfLine;
import java.util.ArrayList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class DefaultSplitCharacter_ESTest extends DefaultSplitCharacter_ESTest_scaffolding {

    // Test Constants
    private static final int INVALID_INDEX = 6724;
    private static final int VALID_INDEX = 4;
    private static final char TEST_CHAR = '1';
    private static final char CUSTOM_SPLIT_CHAR = 'w';
    private static final char HYPHEN = '-';

    @Test(timeout = 4000)
    public void testGetCurrentCharacter_ThrowsExceptionWhenIndexOutOfBounds() throws Throwable {
        // Given: A DefaultSplitCharacter and arrays with limited size
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] smallCharArray = new char[2];
        PdfChunk[] pdfChunks = new PdfChunk[1];
        
        // When & Then: Accessing an invalid index should throw ArrayIndexOutOfBoundsException
        try { 
            splitter.getCurrentCharacter(INVALID_INDEX, smallCharArray, pdfChunks);
            fail("Expected ArrayIndexOutOfBoundsException for index " + INVALID_INDEX);
        } catch(ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacter_ReturnsTrue_ForDefaultSplitCharacterAtIndexZero() throws Throwable {
        // Given: Default split character instance and character array
        char[] charArray = new char[1];
        DefaultSplitCharacter defaultSplitter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;
        
        // When: Checking if character at index 0 is a split character
        boolean isSplitChar = defaultSplitter.isSplitCharacter(0, 0, 0, charArray, null);
        
        // Then: Should return true (null character is a split character)
        assertTrue("Character at index 0 should be a split character", isSplitChar);
    }

    @Test(timeout = 4000)
    public void testPdfLineAddition_WithComplexPdfChunk() throws Throwable {
        // Given: A complex PdfChunk with Greek list symbol and action
        GreekList greekList = new GreekList();
        Chunk symbolChunk = greekList.getSymbol();
        PdfAction action = new PdfAction("[]~N<", true);
        PdfLine pdfLine = new PdfLine(2, 981.5F, 4, 1.0E-6F);
        
        PdfChunk originalChunk = new PdfChunk(symbolChunk, action);
        PdfChunk wrappedChunk = new PdfChunk("(d{2,4}-d{2}-d{2,4})", originalChunk);
        
        // When: Adding chunk to PDF line with negative width
        PdfChunk result = pdfLine.add(wrappedChunk, -1366.4F);
        
        // Then: Should return null (cannot add with negative width)
        assertNull("Adding chunk with negative width should return null", result);
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacter_ReturnsNullCharacter_AtIndexZero() throws Throwable {
        // Given: Default split character and array with null character
        char[] charArray = new char[1]; // Contains '\u0000' by default
        DefaultSplitCharacter defaultSplitter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;
        
        // When: Getting character at index 0
        char currentChar = defaultSplitter.getCurrentCharacter(0, charArray, null);
        
        // Then: Should return null character
        assertEquals("Should return null character", '\u0000', currentChar);
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacter_ReturnsCorrectCharacter_AtValidIndex() throws Throwable {
        // Given: DefaultSplitCharacter and array with specific character at index 4
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] charArray = new char[7];
        charArray[VALID_INDEX] = TEST_CHAR;
        
        // When: Getting character at valid index
        char retrievedChar = splitter.getCurrentCharacter(VALID_INDEX, charArray, null);
        
        // Then: Should return the expected character
        assertEquals("Should return the character at index " + VALID_INDEX, TEST_CHAR, retrievedChar);
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacter_WithCustomSplitCharacter() throws Throwable {
        // Given: DefaultSplitCharacter with custom split character
        DefaultSplitCharacter customSplitter = new DefaultSplitCharacter(CUSTOM_SPLIT_CHAR);
        char[] charArray = new char[2];
        charArray[1] = CUSTOM_SPLIT_CHAR;
        
        // When: Getting character at index 1
        char retrievedChar = customSplitter.getCurrentCharacter(1, charArray, null);
        
        // Then: Should return the custom split character
        assertEquals("Should return custom split character", CUSTOM_SPLIT_CHAR, retrievedChar);
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacter_ThrowsNullPointerException_WhenCharArrayIsNull() throws Throwable {
        // Given: DefaultSplitCharacter with null character array
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        
        // When & Then: Should throw NullPointerException
        try { 
            splitter.isSplitCharacter(505, 505, 505, null, null);
            fail("Expected NullPointerException when character array is null");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacter_ThrowsArrayIndexOutOfBounds_WithNegativeIndex() throws Throwable {
        // Given: DefaultSplitCharacter with empty arrays and negative index
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] emptyCharArray = new char[0];
        PdfChunk[] emptyChunkArray = new PdfChunk[0];
        int negativeIndex = -1743;
        
        // When & Then: Should throw ArrayIndexOutOfBoundsException
        try { 
            splitter.isSplitCharacter(negativeIndex, negativeIndex, negativeIndex, 
                                    emptyCharArray, emptyChunkArray);
            fail("Expected ArrayIndexOutOfBoundsException for negative index");
        } catch(ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacter_ThrowsNullPointerException_WhenCharArrayIsNull() throws Throwable {
        // Given: DefaultSplitCharacter with null character array
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        
        // When & Then: Should throw NullPointerException
        try { 
            splitter.getCurrentCharacter(1522, null, null);
            fail("Expected NullPointerException when character array is null");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacter_ReturnsFalse_ForNonSplitCharacter() throws Throwable {
        // Given: DefaultSplitCharacter with custom unicode character
        DefaultSplitCharacter customSplitter = new DefaultSplitCharacter('\u02FA');
        char[] charArray = new char[51];
        
        // When: Checking if null character is split character
        boolean isSplitChar = customSplitter.isSplitCharacter('\u0000', 0, 0, charArray, null);
        
        // Then: Should return false (custom splitter only splits on '\u02FA')
        assertFalse("Null character should not be split character for custom splitter", isSplitChar);
    }

    @Test(timeout = 4000)
    public void testPdfLineAddition_WithUnicodeChunk() throws Throwable {
        // Given: PdfChunk with unicode replacement character
        GreekList greekList = new GreekList();
        Chunk symbolChunk = greekList.getSymbol();
        PdfAction action = new PdfAction();
        PdfLine pdfLine = new PdfLine(8, 16, 10, 1);
        
        PdfChunk originalChunk = new PdfChunk(symbolChunk, action);
        PdfChunk unicodeChunk = new PdfChunk("\uFFFC", originalChunk); // Unicode replacement character
        
        // When: Adding unicode chunk to PDF line
        PdfChunk result = pdfLine.add(unicodeChunk, 4);
        
        // Then: Should return null
        assertNull("Adding unicode chunk should return null", result);
    }

    @Test(timeout = 4000)
    public void testPdfLineAddition_WithTabSettings() throws Throwable {
        // Given: PdfChunk with tab settings
        GreekList greekList = new GreekList(true, 0);
        Chunk symbolChunk = greekList.getSymbol();
        PdfAction action = new PdfAction();
        TabSettings tabSettings = new TabSettings();
        PdfChunk chunkWithTabs = new PdfChunk(symbolChunk, action, tabSettings);
        
        ArrayList<PdfChunk> chunkList = new ArrayList<PdfChunk>();
        PdfLine pdfLine = new PdfLine(0.0F, 2, 3013.6F, 512, false, chunkList, true);
        
        // When: Adding chunk with tab settings
        PdfChunk result = pdfLine.add(chunkWithTabs);
        
        // Then: Should return null
        assertNull("Adding chunk with tab settings should return null", result);
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacter_ReturnsTrue_ForHyphenCharacter() throws Throwable {
        // Given: Custom DefaultSplitCharacter that includes hyphen as split character
        char[] customSplitChars = new char[9];
        customSplitChars[2] = HYPHEN;
        DefaultSplitCharacter customSplitter = new DefaultSplitCharacter(customSplitChars);
        
        // Create PdfChunk for testing
        GreekList greekList = new GreekList();
        Chunk symbolChunk = greekList.getSymbol();
        PdfAction action = new PdfAction();
        PdfChunk testChunk = new PdfChunk(symbolChunk, action);
        PdfChunk[] chunkArray = new PdfChunk[1];
        chunkArray[0] = testChunk;
        
        // When: Checking if hyphen character is a split character
        boolean isSplitChar = customSplitter.isSplitCharacter(16, 2, 2, customSplitChars, chunkArray);
        
        // Then: Should return true (hyphen is a split character)
        assertTrue("Hyphen should be recognized as split character", isSplitChar);
    }
}