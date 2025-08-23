package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertThrows;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

// Note: The class name and inheritance are kept from the original EvoSuite-generated code.
// In a real-world scenario, the entire test suite would be refactored for better naming and structure.
public class FileTimes_ESTestTest33 extends FileTimes_ESTest_scaffolding {

    /**
     * Tests that {@link FileTimes#minusSeconds(FileTime, long)} throws a NullPointerException
     * when the fileTime parameter is null.
     */
    @Test
    public void minusSeconds_shouldThrowNullPointerException_whenFileTimeIsNull() {
        // The value for secondsToSubtract does not matter since the null check on fileTime occurs first.
        // We use 0L as a neutral value.
        assertThrows(NullPointerException.class, () -> FileTimes.minusSeconds(null, 0L));
    }
}