package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds does not throw an exception for a value that is
     * exactly at the boundary, even when the DateTimeField is null.
     * <p>
     * The method should only access the DateTimeField to build an exception message
     * if the value is out of bounds. Since the value is valid, the null field
     * should not be accessed, and no NullPointerException should be thrown.
     */
    @Test
    public void verifyValueBoundsWithNullFieldSucceedsWhenValueIsOnBoundary() {
        final int boundaryValue = 400;
        
        // This call should succeed. The test passes if no exception is thrown.
        FieldUtils.verifyValueBounds((DateTimeField) null, boundaryValue, boundaryValue, boundaryValue);
    }
}