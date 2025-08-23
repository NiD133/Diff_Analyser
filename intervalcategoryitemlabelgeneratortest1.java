package org.jfree.chart.labels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class, focusing on equality.
 */
@DisplayName("IntervalCategoryItemLabelGenerator equals()")
class IntervalCategoryItemLabelGeneratorTest {

    @Test
    @DisplayName("should return true for two instances created with the default constructor")
    void twoDefaultInstancesShouldBeEqual() {
        // Arrange
        IntervalCategoryItemLabelGenerator generator1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator generator2 = new IntervalCategoryItemLabelGenerator();

        // Assert
        assertEquals(generator1, generator2);
        assertEquals(generator1.hashCode(), generator2.hashCode());
    }

    @Test
    @DisplayName("should return true for instances with identical label format and NumberFormat")
    void instancesWithSameNumberFormatShouldBeEqual() {
        // Arrange
        String labelFormat = "{3} - {4}";
        DecimalFormat numberFormat = new DecimalFormat("0.000");
        IntervalCategoryItemLabelGenerator generator1 = new IntervalCategoryItemLabelGenerator(labelFormat, numberFormat);
        IntervalCategoryItemLabelGenerator generator2 = new IntervalCategoryItemLabelGenerator(labelFormat, numberFormat);

        // Assert
        assertEquals(generator1, generator2);
        assertEquals(generator1.hashCode(), generator2.hashCode());
    }

    @Test
    @DisplayName("should return true for instances with identical label format and DateFormat")
    void instancesWithSameDateFormatShouldBeEqual() {
        // Arrange
        String labelFormat = "{3} - {4}";
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM");
        IntervalCategoryItemLabelGenerator generator1 = new IntervalCategoryItemLabelGenerator(labelFormat, dateFormat);
        IntervalCategoryItemLabelGenerator generator2 = new IntervalCategoryItemLabelGenerator(labelFormat, dateFormat);

        // Assert
        assertEquals(generator1, generator2);
        assertEquals(generator1.hashCode(), generator2.hashCode());
    }

    @Test
    @DisplayName("should return false when comparing a default instance to a custom instance")
    void defaultAndCustomInstancesShouldNotBeEqual() {
        // Arrange
        IntervalCategoryItemLabelGenerator defaultGenerator = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator customGenerator = new IntervalCategoryItemLabelGenerator(
                "{3} - {4}", new DecimalFormat("0.000"));

        // Assert
        assertNotEquals(defaultGenerator, customGenerator);
    }

    @Test
    @DisplayName("should return false for instances with different formatters (Number vs. Date)")
    void instancesWithDifferentFormatterTypesShouldNotBeEqual() {
        // Arrange
        String labelFormat = "{3} - {4}";
        IntervalCategoryItemLabelGenerator numberFormatGenerator = new IntervalCategoryItemLabelGenerator(
                labelFormat, new DecimalFormat("0.000"));
        IntervalCategoryItemLabelGenerator dateFormatGenerator = new IntervalCategoryItemLabelGenerator(
                labelFormat, new SimpleDateFormat("d-MMM"));

        // Assert
        assertNotEquals(numberFormatGenerator, dateFormatGenerator);
    }

    @Test
    @DisplayName("should return false for instances with different label formats")
    void instancesWithDifferentLabelFormatsShouldNotBeEqual() {
        // Arrange
        DecimalFormat formatter = new DecimalFormat("0.000");
        IntervalCategoryItemLabelGenerator generator1 = new IntervalCategoryItemLabelGenerator("{0}", formatter);
        IntervalCategoryItemLabelGenerator generator2 = new IntervalCategoryItemLabelGenerator("{1}", formatter);

        // Assert
        assertNotEquals(generator1, generator2);
    }
}