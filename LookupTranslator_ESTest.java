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
    public void testTranslateWithNoMatch() throws Throwable {
        // Setup a translation table with specific mappings
        CharSequence[][] translationTable = {
            {CharBuffer.wrap(new char[2]), "FFFFF15A"},
            {"FFFFF15A", "FFFFF15A"}
        };
        
        // Create a LookupTranslator with the translation table
        LookupTranslator translator = new LookupTranslator(translationTable);
        
        // Test translating a string that has no match in the table
        String result = translator.translate("FFFFFFFF");
        
        // Verify that the input string is returned unchanged
        assertEquals("FFFFFFFF", result);
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullLookup() throws Throwable {
        CharSequence[][] translationTable = {
            {CharBuffer.wrap(new char[2]), "FFFFF15A"},
            {"FFFFF15A", "FFFFF15A"}
        };

        try {
            // Attempt to create a LookupTranslator with a null entry in the table
            new LookupTranslator(translationTable);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify that a NullPointerException is thrown
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateWithNullTable() throws Throwable {
        StringWriter writer = new StringWriter();
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        CharBuffer input = CharBuffer.wrap("557");

        // Test translating with a null translation table
        int result = translator.translate(input, 0, writer);
        
        // Verify that no characters are translated
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testTranslateWithPartialMatch() throws Throwable {
        CharSequence[][] translationTable = {
            {CharBuffer.wrap(new char[2]), "FFFFF15A"}
        };
        LookupTranslator translator = new LookupTranslator(translationTable);
        StringWriter writer = new StringWriter(1);

        // Test translating a partially matching sequence
        int result = translator.translate(translationTable[0][0], 0, writer);
        
        // Verify that the translation is performed correctly
        assertEquals("FFFFF15A", writer.toString());
        assertEquals(2, result);
    }

    @Test(timeout = 4000)
    public void testStringIndexOutOfBoundsException() throws Throwable {
        CharSequence[][] translationTable = new CharSequence[0][3];
        LookupTranslator translator = new LookupTranslator(translationTable);
        StringWriter writer = new StringWriter();
        StringBuffer buffer = writer.getBuffer();

        try {
            // Attempt to translate with an invalid index
            translator.translate(buffer, 2955, writer);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIndexOutOfBoundsException() throws Throwable {
        CharSequence[][] translationTable = new CharSequence[0][9];
        LookupTranslator translator = new LookupTranslator(translationTable);
        CharBuffer buffer = CharBuffer.allocate(1);
        StringWriter writer = new StringWriter(1);

        try {
            // Attempt to translate with an invalid buffer position
            translator.translate(buffer, 1, writer);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Verify the exception is related to buffer
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testStringIndexOutOfBoundsExceptionInConstructor() throws Throwable {
        CharSequence[][] translationTable = new CharSequence[1][9];
        CharSequence[] sequences = new CharSequence[2];
        StringWriter writer = new StringWriter();
        StringBuffer buffer = writer.getBuffer();
        sequences[1] = buffer;
        translationTable[0] = sequences;
        sequences[0] = buffer;

        try {
            // Attempt to create a LookupTranslator with invalid sequences
            new LookupTranslator(translationTable);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIndexOutOfBoundsExceptionInConstructor() throws Throwable {
        CharSequence[][] translationTable = new CharSequence[1][2];
        CharSequence[] sequences = new CharSequence[2];
        CharBuffer buffer = CharBuffer.wrap(new char[0]);
        sequences[0] = buffer;
        sequences[1] = buffer;
        translationTable[0] = sequences;

        try {
            // Attempt to create a LookupTranslator with invalid sequences
            new LookupTranslator(translationTable);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Verify the exception is related to buffer
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsException() throws Throwable {
        CharSequence[][] translationTable = new CharSequence[1][0];

        try {
            // Attempt to create a LookupTranslator with an empty sequence
            new LookupTranslator(translationTable);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Verify the exception is related to the translation table
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateWithBuffer() throws Throwable {
        CharSequence[] sequences = new CharSequence[3];
        char[] charArray = new char[2];
        charArray[1] = '1';
        CharBuffer buffer1 = CharBuffer.wrap(charArray);
        sequences[0] = buffer1;
        sequences[1] = "FFFFF15A";
        CharBuffer buffer2 = CharBuffer.wrap(sequences[0]);
        CharBuffer buffer3 = CharBuffer.wrap(sequences[1]);
        CharSequence[][] translationTable = new CharSequence[4][7];
        translationTable[0] = sequences;
        translationTable[1] = sequences;
        translationTable[2] = sequences;
        buffer2.get();
        CharSequence[] additionalSequences = new CharSequence[10];
        additionalSequences[0] = buffer2;
        additionalSequences[1] = "FFFFF15A";
        translationTable[3] = additionalSequences;
        LookupTranslator translator = new LookupTranslator(translationTable);

        // Test translating with a buffer
        String result = translator.translate(buffer3);
        
        // Verify the translation result
        assertEquals("FFFFFFFFFF15A5A", result);
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnNullInput() throws Throwable {
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        StringWriter writer = new StringWriter(7);

        try {
            // Attempt to translate a null input
            translator.translate(null, 7, writer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception is related to the translation
            verifyException("org.apache.commons.lang3.text.translate.LookupTranslator", e);
        }
    }
}