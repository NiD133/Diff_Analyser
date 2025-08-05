package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorTest {

    /**
     * Tests the equals() method for consistency and symmetry.
     */
    @Test
    public void testEqualsMethod() {
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator();
        
        // Test equality with default constructors
        assertEquals(generator1, generator2);
        assertEquals(generator2, generator1);

        // Test inequality with different formatters
        generator1 = new IntervalCategoryToolTipGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertNotEquals(generator1, generator2);
        
        generator2 = new IntervalCategoryToolTipGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertEquals(generator1, generator2);

        // Test inequality with different date formatters
        generator1 = new IntervalCategoryToolTipGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertNotEquals(generator1, generator2);
        
        generator2 = new IntervalCategoryToolTipGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertEquals(generator1, generator2);
    }

    /**
     * Verifies that a subclass is not considered equal to an instance of its superclass.
     */
    @Test
    public void testSubclassNotEqualToSuperclass() {
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        StandardCategoryToolTipGenerator standardGenerator = new StandardCategoryToolTipGenerator(
                IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING,
                NumberFormat.getInstance());
        
        assertNotEquals(generator, standardGenerator);
    }

    /**
     * Tests that the hashCode() method is consistent with equals().
     */
    @Test
    public void testHashCodeConsistency() {
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator();
        
        assertEquals(generator1, generator2);
        assertEquals(generator1.hashCode(), generator2.hashCode());
    }

    /**
     * Tests that cloning an instance creates a distinct but equal object.
     */
    @Test
    public void testCloningFunctionality() throws CloneNotSupportedException {
        IntervalCategoryToolTipGenerator original = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    /**
     * Ensures that the class implements the PublicCloneable interface.
     */
    @Test
    public void testImplementsPublicCloneable() {
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        assertTrue(generator instanceof PublicCloneable);
    }

    /**
     * Tests serialization and deserialization for consistency and equality.
     */
    @Test
    public void testSerializationAndDeserialization() {
        IntervalCategoryToolTipGenerator original = new IntervalCategoryToolTipGenerator("{3} - {4}",
                DateFormat.getInstance());
        IntervalCategoryToolTipGenerator deserialized = TestUtils.serialised(original);
        
        assertEquals(original, deserialized);
    }
}