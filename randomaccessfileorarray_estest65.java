package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;
import java.io.IOException;

// Note: The original test class name and inheritance are kept to match the context.
// In a real-world scenario, renaming "RandomAccessFileOrArray_ESTestTest65" to something
// more meaningful like "RandomAccessFileOrArrayTest" would be advisable.
public class RandomAccessFileOrArray_ESTestTest65 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance
     * initialized with a null source throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void readUnsignedInt_whenSourceIsNull_throwsNullPointerException() throws IOException {
        // Arrange: Create an instance with a null underlying source.
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to read from the null source.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        randomAccessFileOrArray.readUnsignedInt();
    }
}