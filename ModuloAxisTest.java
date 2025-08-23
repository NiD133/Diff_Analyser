package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ModuloAxis class with an emphasis on clarity and maintainability.
 */
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ModuloAxisTest {

    private static final String AXIS_LABEL = "Test Axis";
    private static final Range FIXED_RANGE = new Range(0.0, 1.0);

    private static ModuloAxis newDefaultAxis() {
        return new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
    }

    @Test
    public void testCloning_creates_distinct_but_equal_copy() throws CloneNotSupportedException {
        // Arrange
        ModuloAxis original = newDefaultAxis();

        // Act
        ModuloAxis copy = (ModuloAxis) original.clone();

        // Assert
        assertNotSame(original, copy, "Clone should be a different instance");
        assertSame(original.getClass(), copy.getClass(), "Clone should be the same runtime type");
        assertEquals(original, copy, "Clone should be equal in state to the original");
    }

    @Test
    public void testEquals_reflects_changes_to_display_range() {
        // Arrange
        ModuloAxis a1 = newDefaultAxis();
        ModuloAxis a2 = newDefaultAxis();
        assertEquals(a1, a2, "Freshly constructed axes with same inputs should be equal");

        // Act + Assert: modifying one axis' display range should break equality
        a1.setDisplayRange(0.1, 1.1);
        assertNotEquals(a1, a2, "Changing display range on one axis should break equality");

        // Act + Assert: bring the other axis to the same state => equality restored
        a2.setDisplayRange(0.1, 1.1);
        assertEquals(a1, a2, "Axes with the same display range should be equal");
    }

    @Test
    public void testHashCode_same_for_equal_instances() {
        // Arrange
        ModuloAxis a1 = newDefaultAxis();
        ModuloAxis a2 = newDefaultAxis();
        assertEquals(a1, a2, "Precondition: instances must be equal before comparing hash codes");

        // Act
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();

        // Assert
        assertEquals(h1, h2, "Equal objects must have equal hash codes");
    }

    @Test
    public void testSerialization_round_trips_with_equality() {
        // Arrange
        ModuloAxis original = newDefaultAxis();

        // Act
        ModuloAxis restored = TestUtils.serialised(original);

        // Assert
        assertEquals(original, restored, "Deserialised axis should be equal to the original");
    }
}