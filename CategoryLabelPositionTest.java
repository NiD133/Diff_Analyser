package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 *
 * The tests are split by field to clearly show that equals() incorporates
 * every property. Helper factory methods and named constants reduce duplication
 * and improve readability.
 */
public class CategoryLabelPositionTest {

    // Common test constants to make intent explicit
    private static final RectangleAnchor CAT_ANCHOR_BASE = RectangleAnchor.BOTTOM_LEFT;
    private static final RectangleAnchor CAT_ANCHOR_ALT = RectangleAnchor.TOP;

    private static final TextBlockAnchor LABEL_ANCHOR_BASE = TextBlockAnchor.CENTER_RIGHT;
    private static final TextBlockAnchor LABEL_ANCHOR_ALT = TextBlockAnchor.CENTER;

    private static final TextAnchor ROT_ANCHOR_BASE = TextAnchor.BASELINE_LEFT;
    private static final TextAnchor ROT_ANCHOR_ALT = TextAnchor.CENTER;

    private static final double ANGLE_45 = Math.PI / 4.0;
    private static final double ANGLE_30 = Math.PI / 6.0;

    private static final CategoryLabelWidthType WIDTH_TYPE_BASE = CategoryLabelWidthType.RANGE;
    private static final CategoryLabelWidthType WIDTH_TYPE_ALT = CategoryLabelWidthType.CATEGORY;

    private static final float WIDTH_RATIO_BASE = 0.44f;
    private static final float WIDTH_RATIO_ALT = 0.55f;

    private static CategoryLabelPosition pos(RectangleAnchor categoryAnchor,
                                             TextBlockAnchor labelAnchor,
                                             TextAnchor rotationAnchor,
                                             double angle,
                                             CategoryLabelWidthType widthType,
                                             float widthRatio) {
        return new CategoryLabelPosition(categoryAnchor, labelAnchor, rotationAnchor,
                angle, widthType, widthRatio);
    }

    private static CategoryLabelPosition base() {
        return pos(CAT_ANCHOR_BASE, LABEL_ANCHOR_BASE, ROT_ANCHOR_BASE,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
    }

    @Test
    @DisplayName("equals(): identical values yield equality (symmetry check)")
    public void equals_sameValues_areEqual() {
        CategoryLabelPosition p1 = base();
        CategoryLabelPosition p2 = base();
        assertEquals(p1, p2);
        assertEquals(p2, p1);
    }

    @Test
    @DisplayName("equals(): categoryAnchor is part of equality")
    public void equals_considersCategoryAnchor() {
        CategoryLabelPosition base = base();
        CategoryLabelPosition changed = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_BASE, ROT_ANCHOR_BASE,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertNotEquals(base, changed);

        CategoryLabelPosition sameAsChanged = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_BASE, ROT_ANCHOR_BASE,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertEquals(changed, sameAsChanged);
    }

    @Test
    @DisplayName("equals(): labelAnchor is part of equality")
    public void equals_considersLabelAnchor() {
        CategoryLabelPosition base = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_BASE, ROT_ANCHOR_BASE,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        CategoryLabelPosition changed = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_BASE,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertNotEquals(base, changed);

        CategoryLabelPosition sameAsChanged = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_BASE,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertEquals(changed, sameAsChanged);
    }

    @Test
    @DisplayName("equals(): rotationAnchor is part of equality")
    public void equals_considersRotationAnchor() {
        CategoryLabelPosition base = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_BASE,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        CategoryLabelPosition changed = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertNotEquals(base, changed);

        CategoryLabelPosition sameAsChanged = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertEquals(changed, sameAsChanged);
    }

    @Test
    @DisplayName("equals(): angle is part of equality")
    public void equals_considersAngle() {
        CategoryLabelPosition base = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_45, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        CategoryLabelPosition changed = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertNotEquals(base, changed);

        CategoryLabelPosition sameAsChanged = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        assertEquals(changed, sameAsChanged);
    }

    @Test
    @DisplayName("equals(): widthType is part of equality")
    public void equals_considersWidthType() {
        CategoryLabelPosition base = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_BASE, WIDTH_RATIO_BASE);
        CategoryLabelPosition changed = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_ALT, WIDTH_RATIO_BASE);
        assertNotEquals(base, changed);

        CategoryLabelPosition sameAsChanged = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_ALT, WIDTH_RATIO_BASE);
        assertEquals(changed, sameAsChanged);
    }

    @Test
    @DisplayName("equals(): widthRatio is part of equality")
    public void equals_considersWidthRatio() {
        CategoryLabelPosition base = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_ALT, WIDTH_RATIO_BASE);
        CategoryLabelPosition changed = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_ALT, WIDTH_RATIO_ALT);
        assertNotEquals(base, changed);

        CategoryLabelPosition sameAsChanged = pos(CAT_ANCHOR_ALT, LABEL_ANCHOR_ALT, ROT_ANCHOR_ALT,
                ANGLE_30, WIDTH_TYPE_ALT, WIDTH_RATIO_ALT);
        assertEquals(changed, sameAsChanged);
    }

    @Test
    @DisplayName("hashCode(): equal objects must share the same hash code")
    public void hashCode_isConsistentWithEquals() {
        CategoryLabelPosition a1 = new CategoryLabelPosition();
        CategoryLabelPosition a2 = new CategoryLabelPosition();
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    @DisplayName("Serialization round-trip preserves equality")
    public void serialization_roundTrip_preservesEquality() {
        CategoryLabelPosition p1 = new CategoryLabelPosition();
        CategoryLabelPosition p2 = TestUtils.serialised(p1);
        assertEquals(p1, p2);
    }
}