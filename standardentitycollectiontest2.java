package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A test suite for the {@link StandardEntityCollection} class.
 */
class StandardEntityCollectionTest {

    /**
     * Verifies that cloning a StandardEntityCollection creates a new, independent
     * instance with the same initial contents.
     */
    @Test
    @DisplayName("clone() should create an independent copy")
    void clone_shouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange: Create an original collection with one entity.
        PieSectionEntity<String> testEntity = new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                new DefaultPieDataset<String>(), 0, 1, "Key", "ToolTip", "URL");
        
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        originalCollection.add(testEntity);

        // Act: Clone the original collection.
        StandardEntityCollection clonedCollection = (StandardEntityCollection) originalCollection.clone();

        // Assert: The clone should be a different object but have equal content initially.
        assertAll("Verify initial clone properties",
                () -> assertNotSame(originalCollection, clonedCollection, 
                        "The clone should be a new object instance."),
                () -> assertEquals(originalCollection, clonedCollection, 
                        "The clone should be equal to the original before any modifications.")
        );

        // Act & Assert: Modify the original collection to test for independence.
        originalCollection.clear();

        assertNotEquals(originalCollection, clonedCollection, 
                "Modifying the original collection should not affect the clone.");
        assertEquals(1, clonedCollection.getEntityCount(), 
                "The clone should retain its entities after the original is cleared.");
    }
}