package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.io.PolyshapeReader;
import org.locationtech.spatial4j.io.ShapeReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    @Test
    public void addReaderIfNoggitExists_shouldAddReaderWhenListIsEmpty() {
        // Arrange
        SpatialContextFactory factory = new SpatialContextFactory();
        Class<PolyshapeReader> readerClass = PolyshapeReader.class;
        assertTrue("The reader list should be empty before the test", factory.readers.isEmpty());

        // Act
        factory.addReaderIfNoggitExists(readerClass);

        // Assert
        assertEquals("The reader list should contain exactly one reader", 1, factory.readers.size());
        assertTrue("The reader list should contain the added reader class", factory.readers.contains(readerClass));
    }

    @Test
    public void addReaderIfNoggitExists_shouldNotAddDuplicateReader() {
        // Arrange
        SpatialContextFactory factory = new SpatialContextFactory();
        Class<? extends ShapeReader> readerClass = PolyshapeReader.class;
        
        // Pre-populate the list with the reader
        factory.readers.add(readerClass);
        assertEquals("Precondition failed: The reader list should contain one reader", 1, factory.readers.size());

        // Act: Attempt to add the same reader again
        factory.addReaderIfNoggitExists(readerClass);

        // Assert
        assertEquals("A duplicate reader should not have been added", 1, factory.readers.size());
    }
}