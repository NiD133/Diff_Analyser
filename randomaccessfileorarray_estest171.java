package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/*
 * Note: In a real-world scenario, this test class would be renamed to something more
 * descriptive, like `RandomAccessFileOrArrayTest`, and would not extend a scaffolding class
 * unless it provides essential, well-understood functionality.
 */
public class RandomAccessFileOrArray_ESTestTest171 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that constructing a RandomAccessFileOrArray with a null source
     * results in a valid object with a correctly initialized state.
     * Specifically, it checks that the internal source is null and the file pointer is at 0.
     */
    @Test
    public void constructorWithNullSource_initializesStateCorrectly() throws IOException {
        // Arrange: Create an instance with a null RandomAccessSource.
        // The cast is necessary to resolve constructor ambiguity.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Assert: Verify the initial state of the object.
        assertNull("The underlying byte source should be null after construction.", fileOrArray.getByteSource());
        assertEquals("The initial file pointer should be 0.", 0L, fileOrArray.getFilePointer());
    }
}