package org.jfree.chart.entity;

import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the equals() and hashCode() methods of the {@link StandardEntityCollection} class.
 */
@DisplayName("StandardEntityCollection equals and hashCode")
class StandardEntityCollectionEqualsTest {

    private StandardEntityCollection collection1;
    private StandardEntityCollection collection2;

    @BeforeEach
    void setUp() {
        collection1 = new StandardEntityCollection();
        collection2 = new StandardEntityCollection();
    }

    /**
     * Creates a sample PieSectionEntity for use in tests.
     * @return A new PieSectionEntity instance.
     */
    private PieSectionEntity<String> createSampleEntity() {
        Rectangle2D area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        return new PieSectionEntity<>(area, dataset, 0, 1, "Key", "ToolTip", "URL");
    }

    @Test
    @DisplayName("should be equal and have same hash code when both are empty")
    void equals_whenBothCollectionsAreEmpty_thenTheyShouldBeEqual() {
        // Arrange: Two empty collections are created in setUp().

        // Assert
        assertTrue(collection1.equals(collection2), "Two new, empty collections should be equal.");
        assertEquals(collection1.hashCode(), collection2.hashCode(), "Hash codes of empty collections should match.");
    }

    @Test
    @DisplayName("should not be equal when one collection is empty and the other is not")
    void equals_whenOneCollectionHasAnEntity_thenTheyShouldNotBeEqual() {
        // Arrange
        collection1.add(createSampleEntity());

        // Assert
        assertFalse(collection1.equals(collection2), "A collection with an entity should not equal an empty one.");
    }

    @Test
    @DisplayName("should be equal and have same hash code when both contain identical entities")
    void equals_whenBothCollectionsHaveIdenticalEntities_thenTheyShouldBeEqual() {
        // Arrange
        ChartEntity entity1 = createSampleEntity();
        ChartEntity entity2 = createSampleEntity(); // A new, but identical, entity

        collection1.add(entity1);
        collection2.add(entity2);

        // Assert
        assertTrue(collection1.equals(collection2), "Collections with identical entities should be equal.");
        assertEquals(collection1.hashCode(), collection2.hashCode(), "Hash codes should match for equal collections.");
    }

    @Test
    @DisplayName("should not be equal to a non-collection object")
    void equals_whenComparedWithNonCollectionObject_thenShouldReturnFalse() {
        // Arrange: collection1 is an empty StandardEntityCollection.
        Object otherObject = new Object();

        // Assert
        assertFalse(collection1.equals(otherObject), "A collection should not be equal to a non-collection object.");
    }
}