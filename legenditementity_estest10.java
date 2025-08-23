package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link LegendItemEntity} class, focusing on cloning and equality.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that a cloned LegendItemEntity is equal to the original object,
     * but is not the same instance, fulfilling the contract of the clone() method.
     */
    @Test
    public void clonedEntityShouldBeEqualToOriginal() throws CloneNotSupportedException {
        // Arrange: Create an instance of LegendItemEntity.
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<Integer> originalEntity = new LegendItemEntity<>(area);

        // Act: Create a clone of the original entity.
        LegendItemEntity<?> clonedEntity = (LegendItemEntity<?>) originalEntity.clone();

        // Assert: The clone should be a separate instance but equal in value.
        assertNotSame("A cloned object should be a different instance from the original.",
                originalEntity, clonedEntity);
        assertEquals("A cloned object should be equal to the original.",
                originalEntity, clonedEntity);
    }
}