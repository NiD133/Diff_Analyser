package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Tests the equality of two StandardEntityCollection instances.
     * Ensures that the equals method correctly identifies when two collections
     * are equal and when they are not.
     */
    @Test
    public void testEquals() {
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();
        assertEquals(collection1, collection2, "Empty collections should be equal");

        PieSectionEntity<String> entity1 = createPieSectionEntity();
        collection1.add(entity1);
        assertNotEquals(collection1, collection2, "Collections should not be equal after adding an entity to one");

        PieSectionEntity<String> entity2 = createPieSectionEntity();
        collection2.add(entity2);
        assertEquals(collection1, collection2, "Collections should be equal after adding equivalent entities");
    }

    /**
     * Tests the cloning functionality of StandardEntityCollection.
     * Verifies that a cloned collection is equal but not the same instance,
     * and that modifications to the original do not affect the clone.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        PieSectionEntity<String> entity = createPieSectionEntity();
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        originalCollection.add(entity);

        StandardEntityCollection clonedCollection = CloneUtils.clone(originalCollection);
        assertNotSame(originalCollection, clonedCollection, "Cloned collection should be a different instance");
        assertEquals(originalCollection, clonedCollection, "Cloned collection should be equal to the original");

        // Verify independence of the clone
        originalCollection.clear();
        assertNotEquals(originalCollection, clonedCollection, "Cloned collection should remain unchanged after clearing the original");
        clonedCollection.clear();
        assertEquals(originalCollection, clonedCollection, "Both collections should be equal after clearing both");
    }

    /**
     * Tests the serialization and deserialization of StandardEntityCollection.
     * Ensures that a serialized and then deserialized collection is equal to the original.
     */
    @Test
    public void testSerialization() {
        PieSectionEntity<String> entity = createPieSectionEntity();
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        originalCollection.add(entity);

        StandardEntityCollection deserializedCollection = TestUtils.serialised(originalCollection);
        assertEquals(originalCollection, deserializedCollection, "Deserialized collection should be equal to the original");
    }

    /**
     * Helper method to create a PieSectionEntity with predefined properties.
     *
     * @return a new PieSectionEntity instance.
     */
    private PieSectionEntity<String> createPieSectionEntity() {
        return new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                new DefaultPieDataset<>(),
                0, 1, "Key", "ToolTip", "URL"
        );
    }
}