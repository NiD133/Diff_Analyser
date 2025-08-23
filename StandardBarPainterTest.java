package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardBarPainter} class.
 */
@DisplayName("StandardBarPainter")
public class StandardBarPainterTest {

    private static StandardBarPainter newPainter() {
        return new StandardBarPainter();
    }

    @Test
    @DisplayName("equals() follows basic contract for identical (stateless) instances")
    public void equals_basicContract() {
        // Arrange
        StandardBarPainter p1 = newPainter();
        StandardBarPainter p2 = newPainter();

        // Assert
        assertAll("equals() contract",
                () -> assertEquals(p1, p1, "Reflexive: object must equal itself"),
                () -> assertEquals(p1, p2, "Two new StandardBarPainter instances should be equal")
        );
    }

    @Test
    @DisplayName("hashCode() is consistent with equals()")
    public void hashCode_consistentWithEquals() {
        // Arrange
        StandardBarPainter p1 = newPainter();
        StandardBarPainter p2 = newPainter();

        // Act
        int h1 = p1.hashCode();
        int h2 = p2.hashCode();

        // Assert
        assertAll("hashCode/equals consistency",
                () -> assertEquals(p1, p2, "Precondition: instances should be equal"),
                () -> assertEquals(h1, h2, "Equal instances must have equal hash codes")
        );
    }

    @Test
    @DisplayName("Cloning is not supported (object is immutable)")
    public void cloningNotSupported() {
        // Arrange
        StandardBarPainter painter = newPainter();

        // Assert
        assertAll("cloning not supported",
                () -> assertFalse(painter instanceof Cloneable, "Should not implement Cloneable"),
                () -> assertFalse(painter instanceof PublicCloneable, "Should not implement PublicCloneable")
        );
    }

    @Test
    @DisplayName("Serialization round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        // Arrange
        StandardBarPainter original = newPainter();

        // Act
        StandardBarPainter restored = TestUtils.serialised(original);

        // Assert
        assertEquals(original, restored, "Serialized and deserialized instance should be equal");
    }
}