package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfChunk;
import com.itextpdf.text.pdf.PdfLine;
import java.util.ArrayList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DefaultSplitCharacter_ESTest extends DefaultSplitCharacter_ESTest_scaffolding {

    // Tests for getCurrentCharacter() method
    // ======================================

    @Test(timeout = 4000)
    public void getCurrentCharacter_IndexOutOfBounds_ThrowsException() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = new char[2];
        PdfChunk[] chunks = new PdfChunk[1];
        
        try {
            splitter.getCurrentCharacter(6724, chars, chunks);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("6724", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void getCurrentCharacter_NullCharArray_ThrowsException() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        
        try {
            splitter.getCurrentCharacter(1522, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void getCurrentCharacter_ValidIndex_ReturnsCorrectCharacter() {
        DefaultSplitCharacter splitter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;
        char[] chars = new char[1]; // Default value is '\u0000'
        
        char result = splitter.getCurrentCharacter(0, chars, null);
        assertEquals('\u0000', result);
    }

    @Test(timeout = 4000)
    public void getCurrentCharacter_CustomCharacter_ReturnsCorrectCharacter() {
        char testChar = 'w';
        DefaultSplitCharacter splitter = new DefaultSplitCharacter(testChar);
        char[] chars = new char[2];
        chars[1] = testChar;
        
        char result = splitter.getCurrentCharacter(1, chars, null);
        assertEquals(testChar, result);
    }

    // Tests for isSplitCharacter() method
    // ===================================

    @Test(timeout = 4000)
    public void isSplitCharacter_NullCharArray_ThrowsException() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        
        try {
            splitter.isSplitCharacter(505, 505, 505, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void isSplitCharacter_NegativeIndex_ThrowsException() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = new char[0];
        PdfChunk[] chunks = new PdfChunk[0];
        
        try {
            splitter.isSplitCharacter(-1743, -1743, -1743, chars, chunks);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("-1743", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void isSplitCharacter_ControlCharacter_ReturnsTrue() {
        DefaultSplitCharacter splitter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;
        char[] chars = new char[] {'\u0000'};  // Null character (control character)
        
        boolean result = splitter.isSplitCharacter(0, 0, 0, chars, null);
        assertTrue("Control characters should be splittable", result);
    }

    @Test(timeout = 4000)
    public void isSplitCharacter_CustomSplitterWithNonMatchingChar_ReturnsFalse() {
        char customChar = '\u02FA';
        DefaultSplitCharacter splitter = new DefaultSplitCharacter(customChar);
        char[] chars = new char[51]; // All default to '\u0000'
        
        boolean result = splitter.isSplitCharacter(0, 0, 0, chars, null);
        assertFalse("Non-custom characters shouldn't be splittable", result);
    }

    @Test(timeout = 4000)
    public void isSplitCharacter_CustomSplitterWithDash_ReturnsTrue() throws Exception {
        // Setup custom splitter that includes dash
        char[] splitChars = new char[] {'-'};
        DefaultSplitCharacter splitter = new DefaultSplitCharacter(splitChars);
        
        // Create text with dash at position 2
        char[] text = new char[9];
        text[2] = '-';
        
        // Create PDF chunk with sample content
        GreekList list = new GreekList();
        Chunk symbol = list.getSymbol();
        PdfAction action = new PdfAction();
        PdfChunk baseChunk = new PdfChunk(symbol, action);
        PdfChunk[] chunks = new PdfChunk[] {baseChunk};
        
        // Verify splitting behavior at dash position
        boolean result = splitter.isSplitCharacter(16, 2, 2, text, chunks);
        assertTrue("Custom dash character should be splittable", result);
    }
}