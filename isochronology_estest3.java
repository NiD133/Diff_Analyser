package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link ISOChronology}.
 */
public class ISOChronologyTest {

    /**
     * Tests the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_returnsTrue_whenComparingInstanceToItself() {
        // Arrange
        ISOChronology chronology = ISOChronology.getInstanceUTC();

        // Act & Assert
        // An object must be equal to itself. Using assertEquals is more idiomatic
        // for equality checks and provides better failure messages than assertTrue.
        assertEquals(chronology, chronology);
    }
}