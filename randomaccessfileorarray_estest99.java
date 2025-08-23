package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

// The original test class extends a scaffolding class, which is preserved 
// to maintain compatibility with the existing test execution environment.
public class RandomAccessFileOrArray_ESTestTest99 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance
     * after it has been closed throws a NullPointerException.
     * <p>
     * This is the expected behavior, as closing the resource should release
     * internal data structures, rendering subsequent read operations invalid.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void readShouldThrowNullPointerExceptionWhenStreamIsClosed() throws IOException {
        // Arrange: Create an instance from a byte array and then immediately close it.
        byte[] dummyData = new byte[]{42};
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(dummyData);
        fileOrArray.close();

        // Act: Attempt to read from the closed instance.
        // This action is expected to throw a NullPointerException, which is
        // asserted by the 'expected' attribute of the @Test annotation.
        fileOrArray.read();
    }
}