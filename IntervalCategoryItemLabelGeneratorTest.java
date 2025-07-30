package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Tests the equals() method for various scenarios.
     */
    @Test
    public void testEqualsMethod() {
        // Test default constructor
        IntervalCategoryItemLabelGenerator generator1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator generator2 = new IntervalCategoryItemLabelGenerator();
        assertEquals(generator1, generator2, "Default generators should be equal");

        // Test with DecimalFormat
        generator1 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertNotEquals(generator1, generator2, "Generators with different formats should not be equal");

        generator2 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertEquals(generator1, generator2, "Generators with same DecimalFormat should be equal");

        // Test with SimpleDateFormat
        generator1 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertNotEquals(generator1, generator2, "Generators with different formats should not be equal");

        generator2 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertEquals(generator1, generator2, "Generators with same SimpleDateFormat should be equal");
    }

    /**
     * Verifies that the hashCode method is consistent with equals.
     */
    @Test
    public void testHashCodeConsistency() {
        IntervalCategoryItemLabelGenerator generator1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator generator2 = new IntervalCategoryItemLabelGenerator();
        assertEquals(generator1, generator2, "Generators should be equal");
        assertEquals(generator1.hashCode(), generator2.hashCode(), "Hash codes should be equal for equal objects");
    }

    /**
     * Tests the cloning functionality.
     */
    @Test
    public void testCloningFunctionality() throws CloneNotSupportedException {
        IntervalCategoryItemLabelGenerator original = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator clone = (IntervalCategoryItemLabelGenerator) original.clone();
        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should be of the same class");
        assertEquals(original, clone, "Clone should be equal to the original");
    }

    /**
     * Ensures that the class implements PublicCloneable.
     */
    @Test
    public void testImplementsPublicCloneable() {
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        assertTrue(generator instanceof PublicCloneable, "Generator should implement PublicCloneable");
    }

    /**
     * Tests serialization and deserialization for equality.
     */
    @Test
    public void testSerializationDeserialization() {
        IntervalCategoryItemLabelGenerator original = new IntervalCategoryItemLabelGenerator("{3} - {4}", DateFormat.getInstance());
        IntervalCategoryItemLabelGenerator deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized object should be equal to the original");
    }
}