package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

/**
 * Tests for {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitorTest {

    /**
     * Tests that two identical CleaningPathVisitor instances produce the same hash code,
     * adhering to the general contract of Object.hashCode().
     */
    @Test
    public void testHashCodeContract() {
        // Arrange
        // 1. Define the configuration for the visitors.
        final PathCounters pathCounters = Counters.longPathCounters();
        final String[] pathsToSkip = {"", "", "", "", "", ""};

        // 2. Create two separate but identical instances.
        final CleaningPathVisitor visitor1 = new CleaningPathVisitor(pathCounters, pathsToSkip);
        final CleaningPathVisitor visitor2 = new CleaningPathVisitor(pathCounters, pathsToSkip);

        // Act & Assert
        // The hashCode() contract states that if two objects are equal according to the
        // equals() method, then calling hashCode() on each must produce the same integer result.
        assertEquals("Identical visitor instances should be equal.", visitor1, visitor2);
        assertEquals("Hash codes of equal visitor instances should be the same.", visitor1.hashCode(), visitor2.hashCode());
    }
}