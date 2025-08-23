package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    /**
     * Tests the {@code equals} method to ensure it correctly distinguishes between
     * different instances of {@link LegendItemEntity} based on their fields.
     */
    @Test
    public void testEquals() {
        // Create two identical LegendItemEntity instances
        LegendItemEntity<String> entity1 = createLegendItemEntity(1.0, 2.0, 3.0, 4.0);
        LegendItemEntity<String> entity2 = createLegendItemEntity(1.0, 2.0, 3.0, 4.0);
        
        // Verify that the two instances are initially equal
        assertEquals(entity1, entity2);

        // Modify the area and verify inequality
        entity1.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertNotEquals(entity1, entity2);

        // Update the second instance to match and verify equality
        entity2.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertEquals(entity1, entity2);

        // Modify the tooltip text and verify inequality
        entity1.setToolTipText("New ToolTip");
        assertNotEquals(entity1, entity2);

        // Update the second instance to match and verify equality
        entity2.setToolTipText("New ToolTip");
        assertEquals(entity1, entity2);

        // Modify the URL text and verify inequality
        entity1.setURLText("New URL");
        assertNotEquals(entity1, entity2);

        // Update the second instance to match and verify equality
        entity2.setURLText("New URL");
        assertEquals(entity1, entity2);

        // Modify the dataset and verify inequality
        entity1.setDataset(new DefaultCategoryDataset<String, String>());
        assertNotEquals(entity1, entity2);

        // Update the second instance to match and verify equality
        entity2.setDataset(new DefaultCategoryDataset<String, String>());
        assertEquals(entity1, entity2);

        // Modify the series key and verify inequality
        entity1.setSeriesKey("A");
        assertNotEquals(entity1, entity2);

        // Update the second instance to match and verify equality
        entity2.setSeriesKey("A");
        assertEquals(entity1, entity2);
    }

    /**
     * Tests the cloning functionality of {@link LegendItemEntity}.
     * 
     * @throws CloneNotSupportedException if cloning is not supported
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        LegendItemEntity<String> originalEntity = createLegendItemEntity(1.0, 2.0, 3.0, 4.0);
        LegendItemEntity<String> clonedEntity = CloneUtils.clone(originalEntity);

        // Verify that the cloned instance is a different object but equal in content
        assertNotSame(originalEntity, clonedEntity);
        assertSame(originalEntity.getClass(), clonedEntity.getClass());
        assertEquals(originalEntity, clonedEntity);
    }

    /**
     * Tests the serialization and deserialization of {@link LegendItemEntity}.
     */
    @Test
    public void testSerialization() {
        LegendItemEntity<String> originalEntity = createLegendItemEntity(1.0, 2.0, 3.0, 4.0);
        LegendItemEntity<String> deserializedEntity = TestUtils.serialised(originalEntity);

        // Verify that the deserialized instance is equal to the original
        assertEquals(originalEntity, deserializedEntity);
    }

    /**
     * Helper method to create a {@link LegendItemEntity} with a specified area.
     * 
     * @param x the x-coordinate of the area
     * @param y the y-coordinate of the area
     * @param width the width of the area
     * @param height the height of the area
     * @return a new instance of {@link LegendItemEntity}
     */
    private LegendItemEntity<String> createLegendItemEntity(double x, double y, double width, double height) {
        return new LegendItemEntity<>(new Rectangle2D.Double(x, y, width, height));
    }
}