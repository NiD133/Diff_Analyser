package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DefaultSplitCharacter_ESTest extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetCurrentCharacterThrowsArrayIndexOutOfBoundsException() throws Throwable {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] charArray = new char[2];
        PdfChunk[] pdfChunkArray = new PdfChunk[1];

        // Expecting ArrayIndexOutOfBoundsException due to invalid index
        try {
            splitCharacter.getCurrentCharacter(6724, charArray, pdfChunkArray);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacterReturnsTrueForDefaultCharacter() throws Throwable {
        char[] charArray = new char[1];
        DefaultSplitCharacter splitCharacter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;

        // Default behavior should return true for space character
        boolean result = splitCharacter.isSplitCharacter(0, 0, 0, charArray, null);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testPdfLineAddReturnsNullForNonSplittableChunk() throws Throwable {
        GreekList greekList = new GreekList();
        Chunk chunk = greekList.getSymbol();
        PdfAction pdfAction = new PdfAction("[]~N<", true);
        PdfLine pdfLine = new PdfLine(2, 981.5F, 4, 1.0E-6F);
        PdfChunk pdfChunk = new PdfChunk(chunk, pdfAction);
        PdfChunk pdfChunk1 = new PdfChunk("(d{2,4}-d{2}-d{2,4})", pdfChunk);

        // Adding a non-splittable chunk should return null
        PdfChunk result = pdfLine.add(pdfChunk1, -1366.4F);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacterReturnsDefaultCharacter() throws Throwable {
        char[] charArray = new char[1];
        DefaultSplitCharacter splitCharacter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;

        // Default character should be returned
        char result = splitCharacter.getCurrentCharacter(0, charArray, null);
        assertEquals('\u0000', result);
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacterReturnsSpecifiedCharacter() throws Throwable {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] charArray = new char[7];
        charArray[4] = '1';

        // Should return the character at the specified index
        char result = splitCharacter.getCurrentCharacter(4, charArray, null);
        assertEquals('1', result);
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacterWithCustomCharacter() throws Throwable {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter('w');
        char[] charArray = new char[2];
        charArray[1] = 'w';

        // Should return the custom character
        char result = splitCharacter.getCurrentCharacter(1, charArray, null);
        assertEquals('w', result);
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacterThrowsNullPointerException() throws Throwable {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();

        // Expecting NullPointerException due to null character array
        try {
            splitCharacter.isSplitCharacter(505, 505, 505, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacterThrowsArrayIndexOutOfBoundsException() throws Throwable {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] charArray = new char[0];
        PdfChunk[] pdfChunkArray = new PdfChunk[0];

        // Expecting ArrayIndexOutOfBoundsException due to invalid index
        try {
            splitCharacter.isSplitCharacter(-1743, -1743, -1743, charArray, pdfChunkArray);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetCurrentCharacterThrowsNullPointerException() throws Throwable {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();

        // Expecting NullPointerException due to null character array
        try {
            splitCharacter.getCurrentCharacter(1522, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.DefaultSplitCharacter", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacterReturnsFalseForNonSplittableCharacter() throws Throwable {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter('\u02FA');
        char[] charArray = new char[51];

        // Should return false for non-splittable character
        boolean result = splitCharacter.isSplitCharacter('\u0000', 0, 0, charArray, null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testPdfLineAddReturnsNullForSpecialCharacterChunk() throws Throwable {
        GreekList greekList = new GreekList();
        Chunk chunk = greekList.getSymbol();
        PdfAction pdfAction = new PdfAction();
        PdfLine pdfLine = new PdfLine(8, 16, 10, 1);
        PdfChunk pdfChunk = new PdfChunk(chunk, pdfAction);
        PdfChunk pdfChunk1 = new PdfChunk("\uFFFC", pdfChunk);

        // Adding a special character chunk should return null
        PdfChunk result = pdfLine.add(pdfChunk1, 4);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testPdfLineAddReturnsNullForChunkWithTabSettings() throws Throwable {
        GreekList greekList = new GreekList(true, 0);
        Chunk chunk = greekList.getSymbol();
        PdfAction pdfAction = new PdfAction();
        TabSettings tabSettings = new TabSettings();
        PdfChunk pdfChunk = new PdfChunk(chunk, pdfAction, tabSettings);
        ArrayList<PdfChunk> arrayList = new ArrayList<PdfChunk>();
        PdfLine pdfLine = new PdfLine(0.0F, 2, 3013.6F, 512, false, arrayList, true);

        // Adding a chunk with tab settings should return null
        PdfChunk result = pdfLine.add(pdfChunk);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testIsSplitCharacterReturnsTrueForCustomCharacter() throws Throwable {
        GreekList greekList = new GreekList();
        Chunk chunk = greekList.getSymbol();
        PdfAction pdfAction = new PdfAction();
        PdfChunk pdfChunk = new PdfChunk(chunk, pdfAction);
        char[] charArray = new char[9];
        charArray[2] = '-';
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter(charArray);
        PdfChunk[] pdfChunkArray = new PdfChunk[1];
        pdfChunkArray[0] = pdfChunk;

        // Should return true for custom splittable character
        boolean result = splitCharacter.isSplitCharacter(16, 2, 2, charArray, pdfChunkArray);
        assertTrue(result);
    }
}