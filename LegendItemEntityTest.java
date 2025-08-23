package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link LegendItemEntity} class.
 * 
 * The tests are organized to make it clear which field difference causes
 * inequality, and to verify basic equals/clone/serialization behavior.
 */
public class LegendItemEntityTest {

    private static final Rectangle2D DEFAULT_AREA = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);

    private static LegendItemEntity<String> newEntity() {
        return new LegendItemEntity<>(DEFAULT_AREA);
    }

    private static Rectangle2D otherArea() {
        return new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0);
    }

    private static DefaultCategoryDataset<String, String> newCategoryDataset() {
        return new DefaultCategoryDataset<>();
    }

    @Test
    @DisplayName("equals: entities with identical state are equal")
    public void equals_sameState_isEqual() {
        LegendItemEntity<String> a = newEntity();
        LegendItemEntity<String> b = newEntity();

        // basic equals checks
        assertEquals(a, b);
        assertEquals(a, a);            // reflexive
        assertEquals(b, a);            // symmetric
        assertNotEquals(a, null);      // handles null
        assertNotEquals(a, "string");  // handles different type
    }

    @Test
    @DisplayName("equals: differs by area")
    public void equals_differsByArea() {
        LegendItemEntity<String> a = newEntity();
        LegendItemEntity<String> b = newEntity();

        a.setArea(otherArea());
        assertNotEquals(a, b, "Entities with different areas should not be equal");

        b.setArea(otherArea());
        assertEquals(a, b, "Entities with the same area should be equal again");
    }

    @Test
    @DisplayName("equals: differs by tooltip text")
    public void equals_differsByToolTip() {
        LegendItemEntity<String> a = newEntity();
        LegendItemEntity<String> b = newEntity();

        a.setToolTipText("New ToolTip");
        assertNotEquals(a, b, "Entities with different tooltips should not be equal");

        b.setToolTipText("New ToolTip");
        assertEquals(a, b, "Entities with the same tooltip should be equal again");
    }

    @Test
    @DisplayName("equals: differs by URL text")
    public void equals_differsByUrlText() {
        LegendItemEntity<String> a = newEntity();
        LegendItemEntity<String> b = newEntity();

        a.setURLText("New URL");
        assertNotEquals(a, b, "Entities with different URLs should not be equal");

        b.setURLText("New URL");
        assertEquals(a, b, "Entities with the same URL should be equal again");
    }

    @Test
    @DisplayName("equals: differs by dataset")
    public void equals_differsByDataset() {
        LegendItemEntity<String> a = newEntity();
        LegendItemEntity<String> b = newEntity();

        a.setDataset(newCategoryDataset());
        assertNotEquals(a, b, "Entities with different datasets should not be equal");

        b.setDataset(newCategoryDataset());
        assertEquals(a, b, "Entities with equal datasets should be equal again");
    }

    @Test
    @DisplayName("equals: differs by series key")
    public void equals_differsBySeriesKey() {
        LegendItemEntity<String> a = newEntity();
        LegendItemEntity<String> b = newEntity();

        a.setSeriesKey("A");
        assertNotEquals(a, b, "Entities with different series keys should not be equal");

        b.setSeriesKey("A");
        assertEquals(a, b, "Entities with the same series key should be equal again");
    }

    @Test
    @DisplayName("clone: produces a distinct but equal copy")
    public void clone_createsDistinctButEqualCopy() throws CloneNotSupportedException {
        LegendItemEntity<String> original = newEntity();
        LegendItemEntity<String> copy = CloneUtils.clone(original);

        assertNotSame(original, copy, "Clone should be a different instance");
        assertSame(original.getClass(), copy.getClass(), "Clone should have the same type");
        assertEquals(original, copy, "Clone should be equal to the original");
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        LegendItemEntity<String> original = newEntity();
        LegendItemEntity<String> restored = TestUtils.serialised(original);

        assertEquals(original, restored, "Deserialized instance should be equal to the original");
    }
}