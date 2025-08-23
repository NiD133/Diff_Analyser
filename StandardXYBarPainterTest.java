package org.jfree.chart.renderer.xy;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for StandardXYBarPainter.
 * The class is stateless and immutable, so equality/hashCode/serialization
 * behavior is the primary concern. Cloning is intentionally unsupported.
 */
public class StandardXYBarPainterTest {

    private static StandardXYBarPainter newPainter() {
        return new StandardXYBarPainter();
    }

    @Test
    @DisplayName("equals(): obeys the general contract for a stateless painter")
    public void equals_contract() {
        // Arrange
        StandardXYBarPainter a = newPainter();
        StandardXYBarPainter b = newPainter();
        StandardXYBarPainter c = newPainter();

        // Assert
        assertAll(
            () -> assertTrue(a.equals(a), "reflexive"),
            () -> assertTrue(a.equals(b) && b.equals(a), "symmetric"),
            () -> assertTrue(a.equals(b) && b.equals(c) && a.equals(c), "transitive"),
            () -> assertFalse(a.equals(null), "not equal to null"),
            () -> assertFalse(a.equals("not a painter"), "not equal to different type")
        );
    }

    @Test
    @DisplayName("hashCode(): consistent and aligned with equals()")
    public void hashCode_consistencyWithEquals() {
        // Arrange
        StandardXYBarPainter a = newPainter();
        StandardXYBarPainter b = newPainter();

        // Act
        int a1 = a.hashCode();
        int a2 = a.hashCode();
        int bHash = b.hashCode();

        // Assert
        assertAll(
            () -> assertEquals(a1, a2, "hashCode must be consistent for the same instance"),
            () -> assertEquals(a1, bHash, "equal instances must have the same hashCode")
        );
    }

    @Test
    @DisplayName("Cloning: not supported for immutable painter")
    public void cloning_notSupported() {
        // Arrange
        StandardXYBarPainter painter = newPainter();

        // Assert
        assertAll(
            () -> assertFalse(painter instanceof Cloneable, "should not implement Cloneable"),
            () -> assertFalse(painter instanceof PublicCloneable, "should not implement PublicCloneable")
        );
    }

    @Test
    @DisplayName("Serialization: round-trip preserves equality and yields a distinct instance")
    public void serialization_roundTrip() {
        // Arrange
        StandardXYBarPainter original = newPainter();

        // Act
        StandardXYBarPainter copy = TestUtils.serialised(original);

        // Assert
        assertAll(
            () -> assertEquals(original, copy, "serialized copy should be equal to the original"),
            () -> assertNotSame(original, copy, "serialized copy should be a different instance")
        );
    }
}