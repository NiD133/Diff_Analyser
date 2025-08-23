package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the GridArrangement class.
 *
 * Legend for constraint abbreviations used in test names:
 * - N: None (no constraint)
 * - F: Fixed (fixed value)
 * - R: Range (bounded range)
 */
public class GridArrangementTest {

    private static final double EPSILON = 1e-9;

    // Common test data for the single-row container
    private static final int ROWS_1 = 1;
    private static final int COLS_3 = 3;

    private static final double B1_W = 10.0, B1_H = 11.0;
    private static final double B2_W = 20.0, B2_H = 22.0;
    private static final double B3_W = 30.0, B3_H = 33.0;

    private static final double MAX_W = Math.max(B1_W, Math.max(B2_W, B3_W)); // 30
    private static final double MAX_H = Math.max(B1_H, Math.max(B2_H, B3_H)); // 33

    // Helpers -----------------------------------------------------------------

    private static RectangleConstraint fixed(double w, double h) {
        return new RectangleConstraint(w, h);
    }

    private static RectangleConstraint fixedWidth(double w) {
        return RectangleConstraint.NONE.toFixedWidth(w);
    }

    private static RectangleConstraint fixedHeight(double h) {
        return RectangleConstraint.NONE.toFixedHeight(h);
    }

    private static RectangleConstraint widthRange(double minW, double maxW) {
        return RectangleConstraint.NONE.toRangeWidth(new Range(minW, maxW));
    }

    private static RectangleConstraint heightRange(double minH, double maxH) {
        return RectangleConstraint.NONE.toRangeHeight(new Range(minH, maxH));
    }

    private static RectangleConstraint widthRangeFixedHeight(double minW, double maxW, double fixedH) {
        return new RectangleConstraint(new Range(minW, maxW), fixedH);
    }

    private static void assertSize(Size2D size, double expectedW, double expectedH) {
        assertEquals(expectedW, size.getWidth(), EPSILON, "width");
        assertEquals(expectedH, size.getHeight(), EPSILON, "height");
    }

    private static BlockContainer singleRowThreeColsContainer() {
        // 1 row x 3 columns; preferred widths: 10, 20, 30; heights: 11, 22, 33
        BlockContainer c = new BlockContainer(new GridArrangement(ROWS_1, COLS_3));
        c.add(new EmptyBlock(B1_W, B1_H));
        c.add(new EmptyBlock(B2_W, B2_H));
        c.add(new EmptyBlock(B3_W, B3_H));
        return c;
    }

    // Equality / cloning / serialization --------------------------------------

    @Test
    public void equals_distinguishesRowsAndColumns() {
        GridArrangement a = new GridArrangement(11, 22);
        GridArrangement b = new GridArrangement(11, 22);
        assertEquals(a, b);
        assertEquals(b, a);

        a = new GridArrangement(33, 22);
        assertNotEquals(a, b);
        b = new GridArrangement(33, 22);
        assertEquals(a, b);

        a = new GridArrangement(33, 44);
        assertNotEquals(a, b);
        b = new GridArrangement(33, 44);
        assertEquals(a, b);
    }

    @Test
    public void cloning_notSupportedForImmutableClass() {
        GridArrangement a = new GridArrangement(1, 2);
        assertFalse(a instanceof Cloneable);
    }

    @Test
    public void serialization_roundTripsWithEquality() {
        GridArrangement a = new GridArrangement(33, 44);
        GridArrangement b = TestUtils.serialised(a);
        assertEquals(a, b);
    }

    // Layout behavior with a fully populated grid -----------------------------

    @Nested
    @DisplayName("Arrangement for 1 row x 3 columns with 3 blocks")
    class SingleRowThreeCols {

        private BlockContainer c;

        @BeforeEach
        void setUp() {
            c = singleRowThreeColsContainer();
        }

        @Test
        @DisplayName("NN: no width/height constraints")
        void arrange_NN() {
            // width = columns * max(block widths) = 3 * 30 = 90
            // height = rows * max(block heights) = 1 * 33 = 33
            Size2D s = c.arrange(null, RectangleConstraint.NONE);
            assertSize(s, COLS_3 * MAX_W, ROWS_1 * MAX_H);
        }

        @Test
        @DisplayName("FN: fixed width, no height constraint")
        void arrange_FN() {
            Size2D s = c.arrange(null, fixedWidth(100.0));
            // width fixed, height uses preferred = 33
            assertSize(s, 100.0, MAX_H);
        }

        @Test
        @DisplayName("NF: no width constraint, fixed height")
        void arrange_NF() {
            Size2D s = c.arrange(null, fixedHeight(100.0));
            // height fixed, width uses preferred = 90
            assertSize(s, COLS_3 * MAX_W, 100.0);
        }

        @Test
        @DisplayName("RF: width in range, height fixed")
        void arrange_RF() {
            Size2D s = c.arrange(null, widthRangeFixedHeight(40.0, 60.0, 100.0));
            // chooses max width in range to fit => 60, height fixed
            assertSize(s, 60.0, 100.0);
        }

        @Test
        @DisplayName("RR: width in range, height in range")
        void arrange_RR() {
            Size2D s = c.arrange(null,
                    new RectangleConstraint(new Range(40.0, 60.0), new Range(50.0, 70.0)));
            // width takes upper bound = 60, height takes lower bound = 50
            assertSize(s, 60.0, 50.0);
        }

        @Test
        @DisplayName("RN: width in range, no height constraint")
        void arrange_RN() {
            Size2D s = c.arrange(null, widthRange(40.0, 60.0));
            // width takes upper bound, height uses preferred
            assertSize(s, 60.0, MAX_H);
        }

        @Test
        @DisplayName("NR: no width constraint, height in range")
        void arrange_NR() {
            Size2D s = c.arrange(null, heightRange(40.0, 60.0));
            // width uses preferred, height takes lower bound
            assertSize(s, COLS_3 * MAX_W, 40.0);
        }
    }

    // Null block handling ------------------------------------------------------

    @Nested
    @DisplayName("Null block handling in a 1x1 grid")
    class NullBlockHandling {

        private BlockContainer c;

        @BeforeEach
        void setUp() {
            c = new BlockContainer(new GridArrangement(1, 1));
            c.add(null);
        }

        @Test
        @DisplayName("FF: fixed width and height")
        void nullBlock_FF() {
            Size2D s = c.arrange(null, fixed(20.0, 10.0));
            assertSize(s, 20.0, 10.0);
        }

        @Test
        @DisplayName("FN: fixed width, no height constraint")
        void nullBlock_FN() {
            Size2D s = c.arrange(null, fixedWidth(10.0));
            assertSize(s, 10.0, 0.0);
        }

        @Test
        @DisplayName("FR: fixed width, height in range")
        void nullBlock_FR() {
            Size2D s = c.arrange(null, new RectangleConstraint(30.0, new Range(5.0, 10.0)));
            assertSize(s, 30.0, 5.0);
        }

        @Test
        @DisplayName("NN: no width/height constraints")
        void nullBlock_NN() {
            Size2D s = c.arrange(null, RectangleConstraint.NONE);
            assertSize(s, 0.0, 0.0);
        }
    }

    // Handling of grids that are not fully populated --------------------------

    @Nested
    @DisplayName("Grid not full: 2 rows x 3 columns with a single 5x5 block")
    class GridNotFullHandling {

        private BlockContainer c;

        @BeforeEach
        void setUp() {
            Block single = new EmptyBlock(5.0, 5.0);
            c = new BlockContainer(new GridArrangement(2, 3));
            c.add(single);
        }

        @Test
        @DisplayName("FF: fixed width and height")
        void gridNotFull_FF() {
            Size2D s = c.arrange(null, fixed(200.0, 100.0));
            assertSize(s, 200.0, 100.0);
        }

        @Test
        @DisplayName("FN: fixed width, no height constraint")
        void gridNotFull_FN() {
            Size2D s = c.arrange(null, fixedWidth(30.0));
            assertSize(s, 30.0, 10.0);
        }

        @Test
        @DisplayName("FR: fixed width, height in range")
        void gridNotFull_FR() {
            Size2D s = c.arrange(null, new RectangleConstraint(30.0, new Range(5.0, 10.0)));
            assertSize(s, 30.0, 10.0);
        }

        @Test
        @DisplayName("NN: no width/height constraints")
        void gridNotFull_NN() {
            // Preferred width = columns * max item width = 3 * 5 = 15
            // Preferred height = rows * max item height = 2 * 5 = 10
            Size2D s = c.arrange(null, RectangleConstraint.NONE);
            assertSize(s, 15.0, 10.0);
        }
    }
}