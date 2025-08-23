package org.jfree.chart.entity;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link CategoryItemEntity#equals(Object)} method.
 * This suite verifies that the method correctly compares all relevant fields.
 */
@DisplayName("CategoryItemEntity equals contract")
class CategoryItemEntityTest {

    private CategoryItemEntity<String, String> entity1;
    private CategoryItemEntity<String, String> entity2;
    private DefaultCategoryDataset<String, String> dataset;

    /**
     * Sets up two identical CategoryItemEntity instances before each test.
     */
    @BeforeEach
    void setUp() {
        this.dataset = new DefaultCategoryDataset<>();
        this.dataset.addValue(1.0, "Row 1", "Column 1");
        this.dataset.addValue(2.0, "Row 2", "Column 2");

        Shape area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        String toolTipText = "Test ToolTip";
        String urlText = "http://example.com";
        String rowKey = "Row 2";
        String columnKey = "Column 2";

        this.entity1 = new CategoryItemEntity<>(area, toolTipText, urlText, this.dataset, rowKey, columnKey);
        this.entity2 = new CategoryItemEntity<>(area, toolTipText, urlText, this.dataset, rowKey, columnKey);
    }

    @Test
    @DisplayName("An entity should be equal to an identical instance")
    void testEquals_withIdenticalInstance() {
        assertEquals(entity1, entity2, "Entities with identical properties should be equal.");
    }

    @Test
    @DisplayName("An entity should not be equal to null")
    void testEquals_withNull() {
        assertNotEquals(null, entity1, "An entity should not be equal to null.");
    }

    @Test
    @DisplayName("An entity should not be equal to an object of a different type")
    void testEquals_withDifferentObjectType() {
        assertNotEquals(entity1, new Object(), "An entity should not be equal to an object of a different class.");
    }

    @Test
    @DisplayName("Entities with different areas should not be equal")
    void testEquals_whenAreaIsDifferent() {
        entity1.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertNotEquals(entity1, entity2, "Entities should not be equal if their areas differ.");
    }

    @Test
    @DisplayName("Entities with different tool tip texts should not be equal")
    void testEquals_whenToolTipTextIsDifferent() {
        entity1.setToolTipText("A different tooltip");
        assertNotEquals(entity1, entity2, "Entities should not be equal if their tool tip texts differ.");
    }

    @Test
    @DisplayName("Entities with different URL texts should not be equal")
    void testEquals_whenUrlTextIsDifferent() {
        entity1.setURLText("http://another-example.com");
        assertNotEquals(entity1, entity2, "Entities should not be equal if their URL texts differ.");
    }

    @Test
    @DisplayName("Entities with different datasets should not be equal")
    void testEquals_whenDatasetIsDifferent() {
        DefaultCategoryDataset<String, String> differentDataset = new DefaultCategoryDataset<>();
        CategoryItemEntity<String, String> entityWithDifferentDataset = new CategoryItemEntity<>(
                entity1.getArea(), entity1.getToolTipText(), entity1.getURLText(),
                differentDataset, entity1.getRowKey(), entity1.getColumnKey()
        );
        assertNotEquals(entity1, entityWithDifferentDataset, "Entities should not be equal if their datasets differ.");
    }

    @Test
    @DisplayName("Entities with different row keys should not be equal")
    void testEquals_whenRowKeyIsDifferent() {
        CategoryItemEntity<String, String> entityWithDifferentRowKey = new CategoryItemEntity<>(
                entity1.getArea(), entity1.getToolTipText(), entity1.getURLText(),
                this.dataset, "Row 1", entity1.getColumnKey()
        );
        assertNotEquals(entity1, entityWithDifferentRowKey, "Entities should not be equal if their row keys differ.");
    }

    @Test
    @DisplayName("Entities with different column keys should not be equal")
    void testEquals_whenColumnKeyIsDifferent() {
        CategoryItemEntity<String, String> entityWithDifferentColumnKey = new CategoryItemEntity<>(
                entity1.getArea(), entity1.getToolTipText(), entity1.getURLText(),
                this.dataset, entity1.getRowKey(), "Column 1"
        );
        assertNotEquals(entity1, entityWithDifferentColumnKey, "Entities should not be equal if their column keys differ.");
    }
}