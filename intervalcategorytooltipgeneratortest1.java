package org.jfree.chart.labels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class, focusing on the equals() method.
 */
@DisplayName("IntervalCategoryToolTipGenerator.equals()")
class IntervalCategoryToolTipGeneratorTest {

    @Test
    @DisplayName("Two default generators should be equal")
    void testEquals_withDefaultConstructors() {
        // Arrange
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();

        // Assert
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    @DisplayName("Two generators with the same label format and number formatter should be equal")
    void testEquals_withSameNumberFormat() {
        // Arrange
        String labelFormat = "{3} - {4}";
        NumberFormat numberFormat = new DecimalFormat("0.000");
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator(labelFormat, numberFormat);
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator(labelFormat, new DecimalFormat("0.000"));

        // Assert
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    @DisplayName("Two generators with the same label format and date formatter should be equal")
    void testEquals_withSameDateFormat() {
        // Arrange
        String labelFormat = "{3} - {4}";
        DateFormat dateFormat = new SimpleDateFormat("d-MMM");
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator(labelFormat, dateFormat);
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator(labelFormat, new SimpleDateFormat("d-MMM"));

        // Assert
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    @DisplayName("A default generator and a custom generator should not be equal")
    void testEquals_withDefaultAndCustomGenerator() {
        // Arrange
        IntervalCategoryToolTipGenerator defaultGenerator = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator customGenerator = new IntervalCategoryToolTipGenerator(
                "{3} - {4}", new DecimalFormat("0.000"));

        // Assert
        assertNotEquals(defaultGenerator, customGenerator);
    }

    @Test
    @DisplayName("Generators with different formatters should not be equal")
    void testEquals_withDifferentFormatters() {
        // Arrange
        String labelFormat = "{3} - {4}";
        IntervalCategoryToolTipGenerator generatorWithNumberFormat = new IntervalCategoryToolTipGenerator(
                labelFormat, new DecimalFormat("0.000"));
        IntervalCategoryToolTipGenerator generatorWithDateFormat = new IntervalCategoryToolTipGenerator(
                labelFormat, new SimpleDateFormat("d-MMM"));

        // Assert
        assertNotEquals(generatorWithNumberFormat, generatorWithDateFormat);
    }
}