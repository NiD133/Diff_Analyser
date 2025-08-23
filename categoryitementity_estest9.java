package org.jfree.chart.entity;

import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

import javax.swing.JLayeredPane;
import javax.swing.text.DefaultCaret;
import java.awt.Polygon;
import java.awt.Shape;

import static org.junit.Assert.assertNotEquals;

public class CategoryItemEntity_ESTestTest9 extends CategoryItemEntity_ESTest_scaffolding {

    /**
     * Tests that the equals() method returns false when comparing two entities
     * with different attributes.
     *
     * The entities are intentionally created with different areas, URL texts,
     * and row keys to confirm that the equals() method correctly identifies them
     * as non-equal.
     */
    @Test
    public void equals_withDifferentAttributes_returnsFalse() {
        // Arrange: Create two entities that differ in multiple properties.
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        String commonToolTipText = "Shared Tooltip";
        Integer commonColumnKey = 0;

        // First entity with its own distinct properties
        Shape area1 = new DefaultCaret();
        String urlText1 = "http://www.jfree.org/chart1";
        Integer rowKey1 = 0;
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<>(
                area1, commonToolTipText, urlText1, dataset, rowKey1, commonColumnKey);

        // Second entity with different properties
        Shape area2 = new Polygon();
        String urlText2 = "http://www.jfree.org/chart2";
        Integer rowKey2 = JLayeredPane.POPUP_LAYER; // A distinct integer value (300)
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<>(
                area2, commonToolTipText, urlText2, dataset, rowKey2, commonColumnKey);

        // Act & Assert: The two entities should not be considered equal.
        assertNotEquals("Entities with different attributes should not be equal.", entity1, entity2);
    }
}