package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// The original test class name and its base class are preserved.
public class UTF8Writer_ESTestTest22 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that the {@code write(String, int, int)} method throws a
     * {@link StringIndexOutOfBoundsException} when the specified length
     * extends beyond the bounds of the source string.
     */
    @Test(expected = StringIndexOutOfBoundsException.class, timeout = 4000)
    public void writeStringWithOutOfBoundsLengthShouldThrowException() throws IOException {
        // Arrange: Set up the writer and define invalid input parameters.

        // 1. The IOContext is required by the UTF8Writer constructor.
        //    Default values are sufficient as its configuration is not relevant to this test.
        IOContext context = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                null, // ContentReference is not used for this exception path.
                true);

        // 2. Use a simple ByteArrayOutputStream. The actual output is irrelevant
        //    because the exception should be thrown before any writing occurs.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(context, outputStream);

        // 3. Define a string and an offset/length combination that is invalid.
        String text = "test string";
        int offset = 3;
        // A length that, when added to the offset, clearly exceeds the string's length.
        int outOfBoundsLength = 500;

        // Act: Attempt to write using the out-of-bounds parameters.
        // This call is expected to throw the StringIndexOutOfBoundsException.
        writer.write(text, offset, outOfBoundsLength);

        // Assert: The test will fail automatically if the expected exception is not thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}