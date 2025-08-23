package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void getFieldType_shouldReturnYearsDurationFieldType() {
        // Arrange
        Years years = Years.ZERO;
        
        // Act
        DurationFieldType actualFieldType = years.getFieldType();
        
        // Assert
        // The getFieldType() method for a Years object should always return the 'years' singleton.
        assertSame(DurationFieldType.years(), actualFieldType);
    }
}