package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class BoundedReaderTest { //Renamed class for clarity

    @Test(timeout = 4000)
    public void testReadWithInvalidParameters() throws Throwable {
        // 1. Setup: Create a StringReader with a short string ("j").
        StringReader stringReader = new StringReader("j");

        // 2. Setup: Wrap the StringReader with a BoundedReader, limiting the read length to 1.
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);

        // 3. Action: Mark the current position in the reader. This is important for potential resets later, but in this case, it's primarily to set up the state for the test.
        boundedReader.mark(699); // Mark position, argument is readAheadLimit (not directly relevant to the failure)

        // 4. Setup: Create a character array to read into.
        char[] charArray = new char[6];

        // 5. Action: Attempt to read into the char array with invalid parameters.
        //    Specifically, a negative offset (-2537) is used. This should cause an exception.
        try {
            boundedReader.read(charArray, -2537, 699); // Attempt to read with invalid offset

            // 6. Assertion (Expected Failure):  If the read operation *doesn't* throw an exception, then the test has failed.
            fail("Expected ArrayIndexOutOfBoundsException was not thrown."); // Indicate test failure if no exception
        } catch (ArrayIndexOutOfBoundsException e) {
            // 7. Assertion (Expected Success):  The code should throw an ArrayIndexOutOfBoundsException due to the invalid offset.
            //    We *could* add an assertion to check the exception message, but that's often brittle.
            //    Simply catching the exception is enough to verify the behavior in this case.
            // Optionally add a more descriptive assertion if needed:
            assertTrue("Exception message should contain the invalid index", e.getMessage().contains("-2537"));
            // The exception is caught, so the test passes.
        }
    }
}