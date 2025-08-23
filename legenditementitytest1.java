package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link LegendItemEntity} class, focusing on the equals() method.
 *
 * The original monolithic test case was refactored into smaller, focused tests,
 * each verifying a specific condition of the equals() contract. This approach
 * improves readability and maintainability by making the purpose of each test
 * clear and isolating failures to a specific property.
 */
public class LegendItemEntityTest {

    /**
     * Creates a fully-populated LegendItemEntity for use as a baseline in tests.
     *
     * @return A new, fully-initialized LegendItemEntity instance.
     */
    private LegendItemEntity<String> createPopulatedEntity() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        entity.setToolTipText("Test ToolTip");
        entity.setURLText("http://www.jfree.org");
        entity.setDataset(new DefaultCategoryDataset<String, String>());
        entity.setSeriesKey("Series 1");
        return entity;
    }

    @Test
    public void testEquals_withIdenticalInstances() {
        // Arrange
        LegendItemEntity<String> entity1 = createPopulatedEntity();
        LegendItemEntity<String> entity2 = createPopulatedEntity();

        // Act & Assert
        assertEquals(entity1, entity2, "Two identical, fully-populated entities should be equal.");
        assertEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes should be equal for equal objects.");
    }

    @Test
    public void testEquals_whenAreaIsDifferent() {
        // Arrange
        LegendItemEntity<String> entity1 = createPopulatedEntity();
        LegendItemEntity<String> entity2 = createPopulatedEntity();
        
        // Act
        entity2.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));

        // Assert
        assertNotEquals(entity1, entity2, "Entities with different areas should not be equal.");
    }

    @Test
    public void testEquals_whenToolTipTextIsDifferent() {
        // Arrange
        LegendItemEntity<String> entity1 = createPopulatedEntity();
        LegendItemEntity<String> entity2 = createPopulatedEntity();
        
        // Act
        entity2.setToolTipText("Different ToolTip");

        // Assert
        assertNotEquals(entity1, entity2, "Entities with different tooltips should not be equal.");
    }

    @Test
    public void testEquals_whenURLTextIsDifferent() {
        // Arrange
        LegendItemEntity<String> entity1 = createPopulatedEntity();
        LegendItemEntity<String> entity2 = createPopulatedEntity();
        
        // Act
        entity2.setURLText("Different URL");

        // Assert
        assertNotEquals(entity1, entity2, "Entities with different URLs should not be equal.");
    }

    @Test
    public void testEquals_whenDatasetIsDifferent() {
        // Arrange
        LegendItemEntity<String> entity1 = createPopulatedEntity();
        LegendItemEntity<String> entity2 = createPopulatedEntity();
        
        // Act: Create a new dataset with different content
        DefaultCategoryDataset<String, String> differentDataset = new DefaultCategoryDataset<>();
        differentDataset.addValue(99.0, "R9", "C9");
        entity2.setDataset(differentDataset);
        
        // Assert
        assertNotEquals(entity1, entity2, "Entities with different datasets should not be equal.");
    }

    @Test
    public void testEquals_whenSeriesKeyIsDifferent() {
        // Arrange
        LegendItemEntity<String> entity1 = createPopulatedEntity();
        LegendItemEntity<String> entity2 = createPopulatedEntity();
        
        // Act
        entity2.setSeriesKey("Different Key");

        // Assert
        assertNotEquals(entity1, entity2, "Entities with different series keys should not be equal.");
    }
}