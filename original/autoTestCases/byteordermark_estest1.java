package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnderstandableByteOrderMarkTest {

    @Test
    public void testUTF16BEMatchesWithIncorrectInput() {
        // Arrange:
        // Create a ByteOrderMark for UTF-16 Big Endian encoding.
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // An integer array representing input data. The first integer represents the
        // character code of a character.  In this case, the array contains
        // only a single character code, and the unicode value of 65279,
        // which corresponds to the zero-width no-break space (BOM character)
        // that should be present in UTF-16BE data.
        int[] inputData = new int[1]; // Reduced to single element for clarity
        inputData[0] = 65279; // Unicode for U+FEFF

        // Act:
        // Check if the UTF-16BE ByteOrderMark matches the provided input data.
        // Since UTF-16BE usually requires at least two bytes (two integers in int[]),
        // an array of length 1 will not be enough. So the match will fail.
        boolean matches = utf16BE.matches(inputData);

        // Assert:
        // The ByteOrderMark should NOT match because the input data, while containing
        // the BOM character, is not formatted correctly to indicate UTF-16BE. Specifically the input array needs to have at least two elements and be little endian or big endian based on the BOM type.
        assertFalse(matches);
    }
}