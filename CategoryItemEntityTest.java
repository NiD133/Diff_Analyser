package org.jfree.chart.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CategoryItemEntity}.
 *
 * The tests favor readability by:
 * - Using named constants for common values (area, tooltip, URL, keys)
 * - Providing small factory methods to build datasets and entities
 * - Checking equality field-by-field with clear assertion messages
 */
@DisplayName("CategoryItemEntity tests")
public class CategoryItemEntityTest {

    // Common test data to avoid magic literals
    private static final Rectangle2D DEFAULT_AREA = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
    private static final Rectangle2D DIFFERENT_AREA = new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0);

    private static final String DEFAULT_TOOLTIP = "ToolTip";
    private static final String UPDATED_TOOLTIP = "New ToolTip";

    private static final String DEFAULT_URL = "URL";
    private static final String UPDATED_URL = "New URL";

    private static final String R1 = "R1";
    private static final String R2 = "R2";
    private static final String C1 = "C1";
    private static final String C2 = "C2";

    private static DefaultCategoryDataset<String, String> twoByTwoDataset() {
        DefaultCategoryDataset<String, String> d = new DefaultCategoryDataset<>();
        d.addValue(1.0, R1, C1);
        d.addValue(2.0, R1, C2);
        d.addValue(3.0, R2, C1);
        d.addValue(4.0, R2, C2);
        return d;
    }

    private static CategoryItemEntity<String, String> newDefaultEntity(DefaultCategoryDataset<String, String> dataset) {
        return new CategoryItemEntity<>(DEFAULT_AREA, DEFAULT_TOOLTIP, DEFAULT_URL, dataset, R2, C2);
    }

    @Test
    @DisplayName("equals() distinguishes all fields and matches when all fields are equal")
    public void testEquals_comparesAllFields() {
        DefaultCategoryDataset<String, String> ds1 = twoByTwoDataset();
        DefaultCategoryDataset<String, String> ds2 = twoByTwoDataset();

        CategoryItemEntity<String, String> a = newDefaultEntity(ds1);
        CategoryItemEntity<String, String> b = newDefaultEntity(ds1);

        // Sanity: start equal
        assertEquals(a, b, "Entities with identical state should be equal");

        // Area
        a.setArea(DIFFERENT_AREA);
        assertNotEquals(a, b, "Entities should differ when area differs");
        b.setArea(DIFFERENT_AREA);
        assertEquals(a, b, "Entities should be equal after aligning area");

        // Tooltip
        a.setToolTipText(UPDATED_TOOLTIP);
        assertNotEquals(a, b, "Entities should differ when tooltip differs");
        b.setToolTipText(UPDATED_TOOLTIP);
        assertEquals(a, b, "Entities should be equal after aligning tooltip");

        // URL
        a.setURLText(UPDATED_URL);
        assertNotEquals(a, b, "Entities should differ when URL differs");
        b.setURLText(UPDATED_URL);
        assertEquals(a, b, "Entities should be equal after aligning URL");

        // Dataset
        a.setDataset(ds2);
        assertNotEquals(a, b, "Entities should differ when dataset differs");
        b.setDataset(ds2);
        assertEquals(a, b, "Entities should be equal after aligning dataset");

        // Row key
        a.setRowKey(R1);
        assertNotEquals(a, b, "Entities should differ when row key differs");
        b.setRowKey(R1);
        assertEquals(a, b, "Entities should be equal after aligning row key");

        // Column key
        a.setColumnKey(C1);
        assertNotEquals(a, b, "Entities should differ when column key differs");
        b.setColumnKey(C1);
        assertEquals(a, b, "Entities should be equal after aligning column key");
    }

    @Test
    @DisplayName("clone produces a distinct but equal instance")
    public void testCloning() throws CloneNotSupportedException {
        DefaultCategoryDataset<String, String> ds = twoByTwoDataset();
        CategoryItemEntity<String, String> original = newDefaultEntity(ds);

        CategoryItemEntity<String, String> clone = CloneUtils.clone(original);

        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have the same runtime type");
        assertEquals(original, clone, "Clone should be equal to the original");
    }

    @Test
    @DisplayName("serialization round-trip preserves equality")
    public void testSerialization() {
        DefaultCategoryDataset<String, String> ds = twoByTwoDataset();
        CategoryItemEntity<String, String> original = newDefaultEntity(ds);

        CategoryItemEntity<String, String> restored = TestUtils.serialised(original);

        assertEquals(original, restored, "Deserialized instance should be equal to the original");
    }
}