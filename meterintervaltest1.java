package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * A comprehensive test suite for the equals() and hashCode() methods of the MeterInterval class.
 */
@DisplayName("MeterInterval")
class MeterIntervalTest {

    private MeterInterval createDefaultInterval() {
        return new MeterInterval("Label", new Range(1.0, 10.0),
                Color.RED, new BasicStroke(1.0f), Color.BLUE);
    }

    @Nested
    @DisplayName("equals() method")
    class EqualsTest {

        @Test
        @DisplayName("should return true for two identical instances")
        void shouldReturnTrueForIdenticalInstances() {
            MeterInterval interval1 = createDefaultInterval();
            MeterInterval interval2 = createDefaultInterval();
            assertEquals(interval1, interval2);
        }

        @Test
        @DisplayName("should be reflexive")
        void shouldBeReflexive() {
            MeterInterval interval = createDefaultInterval();
            assertEquals(interval, interval);
        }

        @Test
        @DisplayName("should return false when compared with null")
        void shouldReturnFalseForNullComparison() {
            MeterInterval interval = createDefaultInterval();
            assertNotEquals(null, interval);
        }

        @Test
        @DisplayName("should return false when compared with an object of a different type")
        void shouldReturnFalseForDifferentType() {
            MeterInterval interval = createDefaultInterval();
            assertNotEquals("A string", interval);
        }

        @Test
        @DisplayName("should return false if only the labels differ")
        void shouldReturnFalseWhenLabelsDiffer() {
            MeterInterval interval1 = createDefaultInterval();
            MeterInterval interval2 = new MeterInterval("Different Label", new Range(1.0, 10.0),
                    Color.RED, new BasicStroke(1.0f), Color.BLUE);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("should return false if only the ranges differ")
        void shouldReturnFalseWhenRangesDiffer() {
            MeterInterval interval1 = createDefaultInterval();
            MeterInterval interval2 = new MeterInterval("Label", new Range(2.0, 11.0),
                    Color.RED, new BasicStroke(1.0f), Color.BLUE);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("should return false if only the outline paints differ")
        void shouldReturnFalseWhenOutlinePaintsDiffer() {
            MeterInterval interval1 = createDefaultInterval();
            MeterInterval interval2 = new MeterInterval("Label", new Range(1.0, 10.0),
                    Color.GREEN, new BasicStroke(1.0f), Color.BLUE);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("should return false if only the outline strokes differ")
        void shouldReturnFalseWhenOutlineStrokesDiffer() {
            MeterInterval interval1 = createDefaultInterval();
            MeterInterval interval2 = new MeterInterval("Label", new Range(1.0, 10.0),
                    Color.RED, new BasicStroke(2.0f), Color.BLUE);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("should return false if only the background paints differ")
        void shouldReturnFalseWhenBackgroundPaintsDiffer() {
            MeterInterval interval1 = createDefaultInterval();
            MeterInterval interval2 = new MeterInterval("Label", new Range(1.0, 10.0),
                    Color.RED, new BasicStroke(1.0f), Color.YELLOW);
            assertNotEquals(interval1, interval2);
        }
    }

    @Nested
    @DisplayName("hashCode() method")
    class HashCodeTest {

        @Test
        @DisplayName("should return the same hash code for equal objects")
        void shouldReturnSameHashCodeForEqualObjects() {
            MeterInterval interval1 = createDefaultInterval();
            MeterInterval interval2 = createDefaultInterval();

            assertEquals(interval1, interval2, "Precondition: intervals must be equal.");
            assertEquals(interval1.hashCode(), interval2.hashCode());
        }
    }
}