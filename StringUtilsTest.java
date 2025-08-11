package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class StringUtilsTest {

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"Null character (U+0000)", 
                '\u0000', (byte) 0x0, (byte) 0x0},
                
            {"Backspace control character (U+0008)", 
                '\b', (byte) 0x0, (byte) 0x08},
                
            {"ASCII lowercase 'a'", 
                'a', (byte) 0x0, (byte) 0x61},
                
            {"Arabic character 'ة' (U+0629)", 
                'ة', (byte) 0x06, (byte) 0x29},
                
            {"Last character before surrogate range (U+D7FF)", 
                '\ud7ff', (byte) 0xd7, (byte) 0xff},
                
            {"First surrogate character (U+D800)", 
                '\ud800', (byte) 0xd8, (byte) 0x0},
                
            {"Surrogate character (U+DA82)", 
                '\uda82', (byte) 0xda, (byte) 0x82},
                
            {"Surrogate character (U+DBB0)", 
                '\udbb0', (byte) 0xdb, (byte) 0xb0},
                
            {"Last surrogate character (U+DFFF)", 
                '\udfff', (byte) 0xdf, (byte) 0xff},
                
            {"First character after surrogate range (U+E000)", 
                '\ue000', (byte) 0xe0, (byte) 0x0},
                
            {"Replacement character (U+FFFD)", 
                '\ufffd', (byte) 0xff, (byte) 0xfd},
                
            {"Last Basic Multilingual Plane character (U+FFFF)", 
                '\uffff', (byte) 0xff, (byte) 0xff}
        });
    }

    private final char inputChar;
    private final byte expectedHighByte;
    private final byte expectedLowByte;

    public StringUtilsTest(String description, char input, byte highByte, byte lowByte) {
        // Description parameter used for test naming but not in test logic
        this.inputChar = input;
        this.expectedHighByte = highByte;
        this.expectedLowByte = lowByte;
    }

    @Test
    public void convertCharsToBytes_ShouldConvertCharacterToCorrectBytePair() {
        // Prepare
        char[] inputChars = {inputChar};
        byte[] expectedBytes = {expectedHighByte, expectedLowByte};
        
        // Execute
        byte[] actualBytes = StringUtils.convertCharsToBytes(inputChars);
        
        // Verify
        Assert.assertArrayEquals(
            "Character conversion failed for: " + String.format("U+%04X", (int) inputChar),
            expectedBytes, 
            actualBytes
        );
    }
}