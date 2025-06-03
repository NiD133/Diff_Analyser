package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class ByteOrderMarkTest { // Renamed class for better clarity and relation to the subject

    @Test
    public void testUTF16LEDoesNotMatchArbitraryIntArray() {
        // Arrange:  Create a ByteOrderMark for UTF-16LE encoding.
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;

        // Arrange:  Create an arbitrary integer array (filled with zeros in this case).
        //           This array does NOT represent a UTF-16LE byte order mark.
        int[] intArray = {0, 0, 0};

        // Act: Call the 'matches' method to check if the ByteOrderMark matches the array.
        boolean matches = utf16LE.matches(intArray);

        // Assert:  Verify that the 'matches' method returns false, because the array
        //           does not contain the correct byte sequence for a UTF-16LE ByteOrderMark.
        assertFalse(matches);
    }
}