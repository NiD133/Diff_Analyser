package org.jfree.chart.labels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

/**
 * Focused and readable tests for SymbolicXYItemLabelGenerator.
 *
 * These tests verify:
 * - Input validation (null dataset).
 * - Core tooltip formatting for a simple numeric XYDataset.
 * - Equality, hashCode, and clone contracts.
 *
 * They avoid relying on incidental exceptions from specific dataset implementations
 * (which makes tests brittle) and instead validate the generator’s own behavior.
 */
public class SymbolicXYItemLabelGeneratorTest {

    @Test
    public void generateToolTip_throwsIllegalArgumentException_whenDatasetIsNull() {
        SymbolicXYItemLabelGenerator gen = new SymbolicXYItemLabelGenerator();

        try {
            gen.generateToolTip(null, 0, 0);
        } catch (IllegalArgumentException ex) {
            // Message defined by Args.requireNonNull in the implementation.
            assertEquals("Null 'dataset' argument.", ex.getMessage());
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException for null dataset");
    }

    @Test
    public void generateToolTip_formatsNumericValuesFromDataset() {
        // Prepare a minimal numeric XYDataset with a single point (x=102.0, y=0.0)
        DefaultXYDataset dataset = new DefaultXYDataset();
        double[][] seriesData = new double[][] {
            { 102.0 },  // x values
            { 0.0 }     // y values
        };
        dataset.addSeries("s", seriesData);

        SymbolicXYItemLabelGenerator gen = new SymbolicXYItemLabelGenerator();

        String tooltip = gen.generateToolTip(dataset, 0, 0);
        assertEquals("X: 102.0, Y: 0.0", tooltip);
    }

    @Test
    public void equals_returnsFalseForDifferentType_andTrueForSelf() {
        SymbolicXYItemLabelGenerator gen = new SymbolicXYItemLabelGenerator();

        assertFalse(gen.equals(new Object()));
        assertTrue(gen.equals(gen));
    }

    @Test
    public void clone_producesEqualInstance_withConsistentHashCode() throws CloneNotSupportedException {
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        Object copy = original.clone();

        assertTrue(original.equals(copy));
        assertEquals(original.hashCode(), copy.hashCode());
    }

    // Optional utility to create a one-point dataset (kept here for readability
    // should more tests be added in the future).
    private static XYDataset onePointDataset(double x, double y) {
        DefaultXYDataset d = new DefaultXYDataset();
        d.addSeries("s", new double[][] { { x }, { y } });
        return d;
    }
}