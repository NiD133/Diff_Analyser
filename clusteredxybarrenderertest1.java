package org.jfree.chart.renderer.xy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class, focusing on the equals() contract.
 */
@DisplayName("ClusteredXYBarRenderer equals() Contract")
class ClusteredXYBarRendererTest {

    @Test
    @DisplayName("should be equal to itself")
    void testEquals_sameInstance() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        assertEquals(renderer, renderer, "A renderer instance should be equal to itself.");
    }

    @Test
    @DisplayName("should return false when compared with null")
    void testEquals_withNull() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        assertNotEquals(null, renderer, "A renderer instance should not be equal to null.");
    }

    @Test
    @DisplayName("should return false when compared with a different object type")
    void testEquals_withDifferentObjectType() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        assertNotEquals(renderer, new Object(), "A renderer instance should not be equal to an object of a different type.");
    }

    @Test
    @DisplayName("should be true for two separate default instances")
    void testEquals_defaultInstances() {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        assertEquals(renderer1, renderer2, "Two default-constructed renderers should be equal.");
    }

    @Test
    @DisplayName("should be true for two identical non-default instances")
    void testEquals_identicalNonDefaultInstances() {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(0.15, true);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(0.15, true);
        assertEquals(renderer1, renderer2, "Two renderers with identical properties should be equal.");
    }

    @Test
    @DisplayName("should be false when 'centerBarAtStartValue' differs")
    void testEquals_whenCenterBarAtStartValueDiffers() {
        // Arrange
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(0.15, true);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(0.15, false);

        // Act & Assert
        assertNotEquals(renderer1, renderer2, "Renderers should not be equal if 'centerBarAtStartValue' differs.");
    }

    @Test
    @DisplayName("should be false when 'margin' (from superclass) differs")
    void testEquals_whenMarginDiffers() {
        // Arrange
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(0.15, true);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(0.20, true);

        // Act & Assert
        assertNotEquals(renderer1, renderer2, "Renderers should not be equal if the 'margin' property differs.");
    }
}