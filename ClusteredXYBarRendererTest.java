package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, intention-revealing tests for ClusteredXYBarRenderer.
 *
 * What is covered:
 * - equals()/hashCode() contracts (default, margin, and center-at-start flag)
 * - Cloning and PublicCloneable contract
 * - Java serialization round-trip
 * - Domain bounds calculation with and without centering at start value
 */
class ClusteredXYBarRendererTest {

    private static final double EPSILON = 1e-10;

    // ------------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------------

    private static ClusteredXYBarRenderer renderer() {
        return new ClusteredXYBarRenderer(); // margin = 0.0, centerBarAtStartValue = false
    }

    private static ClusteredXYBarRenderer renderer(double margin, boolean centerAtStart) {
        return new ClusteredXYBarRenderer(margin, centerAtStart);
    }

    private static DefaultIntervalXYDataset<String> sampleDataset() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();

        // Series S1
        double[] x1 = {1.0, 2.0, 3.0};
        double[] x1Start = {0.9, 1.9, 2.9};
        double[] x1End = {1.1, 2.1, 3.1};
        double[] y1 = {4.0, 5.0, 6.0};
        double[] y1Start = {1.09, 2.09, 3.09};
        double[] y1End = {1.11, 2.11, 3.11};
        dataset.addSeries("S1", new double[][] {x1, x1Start, x1End, y1, y1Start, y1End});

        // Series S2
        double[] x2 = {11.0, 12.0, 13.0};
        double[] x2Start = {10.9, 11.9, 12.9};
        double[] x2End = {11.1, 12.1, 13.1};
        double[] y2 = {14.0, 15.0, 16.0};
        double[] y2Start = {11.09, 12.09, 13.09};
        double[] y2End = {11.11, 12.11, 13.11};
        dataset.addSeries("S2", new double[][] {x2, x2Start, x2End, y2, y2Start, y2End});

        return dataset;
    }

    // ------------------------------------------------------------------------
    // equals() and hashCode()
    // ------------------------------------------------------------------------

    @Nested
    class EqualsAndHashCode {

        @Test
        @DisplayName("Default renderers are equal and symmetric")
        void defaultRenderers_areEqual() {
            ClusteredXYBarRenderer a = renderer();
            ClusteredXYBarRenderer b = renderer();

            assertEquals(a, b);
            assertEquals(b, a);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("Different margins -> not equal")
        void differentMargin_notEqual() {
            ClusteredXYBarRenderer a = renderer(1.2, false);
            ClusteredXYBarRenderer b = renderer(); // margin = 0.0

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("Same margin and same center-at-start flag -> equal")
        void sameMarginAndCenterFlag_equal() {
            ClusteredXYBarRenderer a = renderer(1.2, false);
            ClusteredXYBarRenderer b = renderer(1.2, false);

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("Different center-at-start flag -> not equal")
        void differentCenterFlag_notEqual() {
            ClusteredXYBarRenderer a = renderer(1.2, true);
            ClusteredXYBarRenderer b = renderer(1.2, false);

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("Same margin and center-at-start flag (true) -> equal")
        void sameMarginAndCenterFlagTrue_equal() {
            ClusteredXYBarRenderer a = renderer(1.2, true);
            ClusteredXYBarRenderer b = renderer(1.2, true);

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    // ------------------------------------------------------------------------
    // Cloning and PublicCloneable
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Cloning produces an equal but distinct instance")
    void cloning() throws CloneNotSupportedException {
        ClusteredXYBarRenderer original = renderer();
        ClusteredXYBarRenderer clone = CloneUtils.clone(original);

        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    @Test
    @DisplayName("Implements PublicCloneable")
    void publicCloneable() {
        assertTrue(renderer() instanceof PublicCloneable);
    }

    // ------------------------------------------------------------------------
    // Serialization
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Serialization round-trip preserves equality")
    void serialization() {
        ClusteredXYBarRenderer original = renderer();
        ClusteredXYBarRenderer restored = TestUtils.serialised(original);

        assertEquals(original, restored);
    }

    // ------------------------------------------------------------------------
    // Domain bounds
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("findDomainBounds: default renderer (centerBarAtStart=false)")
    void findDomainBounds_default() {
        AbstractXYItemRenderer r = renderer(); // centerBarAtStartValue = false
        XYDataset<String> dataset = sampleDataset();

        // With centerBarAtStart=false, bounds should reflect interval end values
        Range bounds = r.findDomainBounds(dataset);

        assertAll(
                () -> assertEquals(0.9, bounds.getLowerBound(), EPSILON),
                () -> assertEquals(13.1, bounds.getUpperBound(), EPSILON)
        );
    }

    @Test
    @DisplayName("findDomainBounds: center bars at start value")
    void findDomainBounds_centerBarAtStart() {
        AbstractXYItemRenderer r = renderer(0.0, true);
        XYDataset<String> dataset = sampleDataset();

        // With centerBarAtStart=true, bounds shift to start values
        Range bounds = r.findDomainBounds(dataset);

        assertAll(
                () -> assertEquals(0.8, bounds.getLowerBound(), EPSILON),
                () -> assertEquals(13.0, bounds.getUpperBound(), EPSILON)
        );
    }

    @Test
    @DisplayName("findDomainBounds: null dataset returns null")
    void findDomainBounds_nullDataset() {
        AbstractXYItemRenderer r = renderer(0.0, true);

        assertNull(r.findDomainBounds(null));
    }
}