package org.jfree.chart.entity;

import org.jfree.chart.TestUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the serialization of the {@link StandardEntityCollection} class.
 */
class StandardEntityCollectionTest {

    /**
     * Verifies that a StandardEntityCollection object can be serialized and
     * then deserialized, resulting in an object that is equal to the original.
     */
    @Test
    @DisplayName("A serialized and deserialized collection should be equal to the original")
    void collectionAfterSerializationShouldBeEqualToOriginal() {
        // Arrange: Create an entity and add it to a collection.
        PieSectionEntity<String> testEntity = new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                new DefaultPieDataset<>(), 0, 1, "Key", "ToolTip", "URL");
        
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        originalCollection.add(testEntity);

        // Act: Serialize and then deserialize the collection.
        StandardEntityCollection deserializedCollection = TestUtils.serialised(originalCollection);

        // Assert: The deserialized collection should be a different instance but equal to the original.
        assertNotSame(originalCollection, deserializedCollection, "Serialization should create a new instance.");
        assertEquals(originalCollection, deserializedCollection, "Deserialized collection should be equal to the original.");
    }
}