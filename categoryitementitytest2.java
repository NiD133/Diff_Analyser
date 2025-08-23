package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on cloning behavior.
 */
class CategoryItemEntityTest {

    /**
     * Verifies that cloning a CategoryItemEntity creates a new instance
     * that is equal to the original but is not the same object reference.
     */
    @Test
    @DisplayName("A cloned CategoryItemEntity should be equal to the original but independent")
    void clone_shouldBeEqualToOriginalButNotTheSameInstance() throws CloneNotSupportedException {
        // Arrange: Create a dataset and an entity to be cloned.
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(4.0, "R2", "C2");

        CategoryItemEntity<String, String> originalEntity = new CategoryItemEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                "ToolTip Text",
                "URL Text",
                dataset,
                "R2",
                "C2"
        );

        // Act: Clone the original entity.
        CategoryItemEntity<String, String> clonedEntity = CloneUtils.clone(originalEntity);

        // Assert: Verify the properties of the cloned entity.
        assertNotSame(originalEntity, clonedEntity, "The cloned entity should be a new object instance.");
        assertEquals(originalEntity, clonedEntity, "The cloned entity should be equal to the original in value.");
    }
}