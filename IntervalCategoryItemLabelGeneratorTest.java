package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable and maintainable tests for IntervalCategoryItemLabelGenerator.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    private static final String FORMAT = "{3} - {4}";

    // Factory helpers make intent explicit in tests.
    private static IntervalCategoryItemLabelGenerator newDefault() {
        return new IntervalCategoryItemLabelGenerator();
    }

    private static IntervalCategoryItemLabelGenerator newWithNumberFormat() {
        return new IntervalCategoryItemLabelGenerator(FORMAT, new DecimalFormat("0.000"));
    }

    private static IntervalCategoryItemLabelGenerator newWithDateFormat() {
        return new IntervalCategoryItemLabelGenerator(FORMAT, new SimpleDateFormat("d-MMM"));
    }

    @Test
    @DisplayName("equals(): default instances are equal and symmetric")
    public void equals_defaultInstancesAreEqual() {
        // arrange
        IntervalCategoryItemLabelGenerator g1 = newDefault();
        IntervalCategoryItemLabelGenerator g2 = newDefault();

        // assert
        assertEquals(g1, g1, "Reflexive property failed");
        assertEquals(g1, g2, "Two default instances should be equal");
        assertEquals(g2, g1, "Equality should be symmetric");
    }

    @Test
    @DisplayName("equals(): instances with same number format are equal")
    public void equals_instancesWithSameNumberFormatAreEqual() {
        // arrange
        IntervalCategoryItemLabelGenerator base = newDefault();
        IntervalCategoryItemLabelGenerator g1 = newWithNumberFormat();

        // assert: different configuration from default
        assertNotEquals(base, g1, "Default and number-format instances should differ");

        // arrange: construct a second instance with the same number format
        IntervalCategoryItemLabelGenerator g2 = newWithNumberFormat();

        // assert: equal when config matches
        assertEquals(g1, g2, "Instances with identical number format and pattern should be equal");
    }

    @Test
    @DisplayName("equals(): instances with same date format are equal")
    public void equals_instancesWithSameDateFormatAreEqual() {
        // arrange
        IntervalCategoryItemLabelGenerator g1 = newWithDateFormat();
        IntervalCategoryItemLabelGenerator g2 = newWithDateFormat();

        // assert
        assertEquals(g1, g2, "Instances with identical date format and pattern should be equal");
    }

    @Test
    @DisplayName("equals(): number-format and date-format instances are not equal")
    public void equals_numberAndDateFormatInstancesDiffer() {
        // arrange
        IntervalCategoryItemLabelGenerator numberFmt = newWithNumberFormat();
        IntervalCategoryItemLabelGenerator dateFmt = newWithDateFormat();

        // assert
        assertNotEquals(numberFmt, dateFmt, "Different formatter types should not be equal");
    }

    @Test
    @DisplayName("hashCode(): equal instances have identical hash codes")
    public void hashCode_consistentForEqualObjects() {
        // arrange
        IntervalCategoryItemLabelGenerator g1 = newDefault();
        IntervalCategoryItemLabelGenerator g2 = newDefault();

        // assert
        assertEquals(g1, g2, "Precondition: instances must be equal");
        assertEquals(g1.hashCode(), g2.hashCode(), "Equal objects must have equal hash codes");
    }

    @Test
    @DisplayName("clone(): produces a distinct but equal instance")
    public void clone_createsEqualButDistinctInstance() throws CloneNotSupportedException {
        // arrange
        IntervalCategoryItemLabelGenerator original = newDefault();

        // act
        IntervalCategoryItemLabelGenerator clone =
                (IntervalCategoryItemLabelGenerator) original.clone();

        // assert
        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should be of the same type");
        assertEquals(original, clone, "Clone and original should be equal");
    }

    @Test
    @DisplayName("PublicCloneable: class advertises public cloning support")
    public void implementsPublicCloneable() {
        assertTrue(newDefault() instanceof PublicCloneable,
                "Generator should implement PublicCloneable");
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        // arrange
        IntervalCategoryItemLabelGenerator original =
                new IntervalCategoryItemLabelGenerator(FORMAT, DateFormat.getInstance());

        // act
        IntervalCategoryItemLabelGenerator restored = TestUtils.serialised(original);

        // assert
        assertNotNull(restored, "Deserialized instance should not be null");
        assertEquals(original, restored, "Serialized and deserialized instances should be equal");
    }
}