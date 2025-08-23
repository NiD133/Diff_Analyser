package org.apache.commons.io.file.attribute;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that FileTimes.minusMillis() throws a NullPointerException when the
     * input FileTime is null.
     */
    @Test(expected = NullPointerException.class)
    public void minusMillisShouldThrowNullPointerExceptionForNullInput() {
        // The value for millisToSubtract is arbitrary as it's not relevant to the null check.
        final long arbitraryMillis = 2332L;

        // Act & Assert: Call the method with a null FileTime, which is expected to throw.
        FileTimes.minusMillis(null, arbitraryMillis);
    }
}