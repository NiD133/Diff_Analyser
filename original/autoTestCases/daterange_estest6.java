package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jfree.data.Range;

public class DateRangeTest {

    @Test(timeout = 4000)
    public void testConstructorWithNullRangeThrowsException() {
        // Arrange: Define the input (a null Range).
        Range nullRange = null;

        // Act: Attempt to create a DateRange object using the null Range.
        try {
            new DateRange(nullRange);

            // Assert:  The code should NOT reach here.  If it does, the test fails because no exception was thrown.
            fail("Expected NullPointerException to be thrown when constructing DateRange with a null Range.");

        } catch (NullPointerException e) {
            // Assert: Check that a NullPointerException was thrown as expected.
            // The exception's message doesn't need to be checked in this simplified version.  We only care that the correct exception type was thrown.
            // In real-world scenarios, you might check the exception message as well for more specific validation.

            // Alternatively, if you need to assert specifically the exception was thrown by DateRange itself:
            //assertTrue("Exception should be thrown by DateRange constructor", e.getStackTrace()[0].getClassName().equals("org.example.DateRange"));
        }
    }
}