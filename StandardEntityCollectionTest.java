package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the StandardEntityCollection class.
 *
 * The tests use a single PieSectionEntity instance (or an equivalent one)
 * to keep the focus on the collection behavior (equals/clone/serialization)
 * rather than the specifics of the entity type.
 */
class StandardEntityCollectionTest {

    // Test data factory to keep entity creation consistent and intention-revealing.
    private static PieSectionEntity<String> newPieEntity() {
        return new PieSectionEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                new DefaultPieDataset<>(),
                0,  // dataset index
                1,  // section index
                "Key",
                "ToolTip",
                "URL"
        );
    }

    @Test
    @DisplayName("equals(): empty collections are equal; adding entity affects equality; same contents restore equality")
    void equals_respectsContents() {
        // Arrange
        StandardEntityCollection c1 = new StandardEntityCollection();
        StandardEntityCollection c2 = new StandardEntityCollection();

        // Assert: two empty collections are equal
        assertEquals(c1, c2, "Two empty collections should be equal");

        // Act: add an entity to one collection only
        c1.add(newPieEntity());

        // Assert: collections now differ
        assertNotEquals(c1, c2, "Collections should differ after adding an entity to one of them");

        // Act: add an equal entity to the second collection
        c2.add(newPieEntity());

        // Assert: collections are equal again (same contents)
        assertEquals(c1, c2, "Collections with the same contents should be equal");
    }

    @Test
    @DisplayName("clone(): produces an independent copy with equal contents")
    void cloning_createsIndependentEqualCopy() throws CloneNotSupportedException {
        // Arrange
        StandardEntityCollection original = new StandardEntityCollection();
        original.add(newPieEntity());

        // Act
        StandardEntityCollection clone = CloneUtils.clone(original);

        // Assert: basic clone properties
        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have the same runtime type");
        assertEquals(original, clone, "Clone should be equal to the original");

        // Assert: independence (mutating one does not affect the other)
        original.clear();
        assertNotEquals(original, clone, "Clearing the original should not clear the clone");
        clone.clear();
        assertEquals(original, clone, "After clearing both, they should be equal again");
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    void serialization_roundTripPreservesEquality() {
        // Arrange
        StandardEntityCollection original = new StandardEntityCollection();
        original.add(newPieEntity());

        // Act
        StandardEntityCollection restored = TestUtils.serialised(original);

        // Assert
        assertEquals(original, restored, "Deserialized collection should be equal to the original");
    }
}