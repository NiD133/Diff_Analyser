package org.joda.time.base;

import org.joda.time.DateTimeField;
import org.joda.time.Partial;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the getFields() method in AbstractPartial, using Partial as the concrete implementation.
 */
public class AbstractPartialTest {

    @Test
    public void getFields_onEmptyPartial_returnsEmptyArray() {
        // Arrange: Create an empty Partial instance, which contains no fields.
        Partial emptyPartial = new Partial();

        // Act: Retrieve the array of DateTimeField objects.
        DateTimeField[] fields = emptyPartial.getFields();

        // Assert: The returned array should be empty.
        assertEquals("An empty Partial should have no fields.", 0, fields.length);
    }
}