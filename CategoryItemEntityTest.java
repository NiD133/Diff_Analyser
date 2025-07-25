package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryItemEntity} class.  These tests verify the
 * equals method, cloning, and serialization.
 */
public class CategoryItemEntityTest {

    private DefaultCategoryDataset<String, String> dataset;
    private CategoryItemEntity<String, String> entity;

    @BeforeEach
    public void setUp() {
        // Create a sample dataset for use in the tests
        dataset = new DefaultCategoryDataset<>();
        dataset.addValue(1.0, "Row1", "Column1");
        dataset.addValue(2.0, "Row1", "Column2");
        dataset.addValue(3.0, "Row2", "Column1");
        dataset.addValue(4.0, "Row2", "Column2");

        // Create a sample CategoryItemEntity for use in the tests
        entity = new CategoryItemEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                "ToolTipText",
                "URLText",
                dataset,
                "Row2",
                "Column2"
        );
    }

    /**
     * Confirms that the equals method distinguishes all relevant fields.
     */
    @Test
    public void testEquals() {
        CategoryItemEntity<String, String> equalEntity = new CategoryItemEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                "ToolTipText",
                "URLText",
                dataset,
                "Row2",
                "Column2"
        );
        assertEquals(entity, equalEntity, "Entities should be equal when all fields match.");

        // Test inequality with different area
        equalEntity.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertNotEquals(entity, equalEntity, "Entities should not be equal with different areas.");
        equalEntity.setArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0)); // Reset for next test
        assertEquals(entity, equalEntity);


        // Test inequality with different tooltip
        equalEntity.setToolTipText("Different ToolTip");
        assertNotEquals(entity, equalEntity, "Entities should not be equal with different tooltips.");
        equalEntity.setToolTipText("ToolTipText"); // Reset
        assertEquals(entity, equalEntity);

        // Test inequality with different URL
        equalEntity.setURLText("Different URL");
        assertNotEquals(entity, equalEntity, "Entities should not be equal with different URLs.");
        equalEntity.setURLText("URLText"); // Reset
        assertEquals(entity, equalEntity);
    }

    /**
     * Confirms that cloning produces a new object with the same field values.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        CategoryItemEntity<String, String> clonedEntity = CloneUtils.clone(entity);

        assertNotSame(entity, clonedEntity, "Cloned entity should be a different object.");
        assertSame(entity.getClass(), clonedEntity.getClass(), "Cloned entity should be of the same class.");
        assertEquals(entity, clonedEntity, "Cloned entity should be equal to the original.");
    }

    /**
     * Confirms that serialization and deserialization produce an equal object.
     */
    @Test
    public void testSerialization() {
        CategoryItemEntity<String, String> deserializedEntity = TestUtils.serialised(entity);
        assertEquals(entity, deserializedEntity, "Deserialized entity should be equal to the original.");
    }

}