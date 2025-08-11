You are a professional software engineer. Your task is to improve the understandability of the following test code, which may be either a single test case or a full test suite. Understandability means the ease with which developers can comprehend and maintain the test code.

Here is the original test code:

/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------------------------
 * CategoryLabelPositionTest.java
 * ------------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {
    
    /**
     * Check that the equals() method can distinguish all fields.
     */
    @Test
    public void testEquals() {
        CategoryLabelPosition p1 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        CategoryLabelPosition p2 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);
        assertEquals(p2, p1);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT,
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT,
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER,  Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertEquals(p1, p2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        CategoryLabelPosition a1 = new CategoryLabelPosition();
        CategoryLabelPosition a2 = new CategoryLabelPosition();
        assertEquals(a1, a2);
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        CategoryLabelPosition p1 = new CategoryLabelPosition();
        CategoryLabelPosition p2 = TestUtils.serialised(p1);
        assertEquals(p1, p2);
    }

}

Here is the source class under test:

/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * --------------------------
 * CategoryLabelPosition.java
 * --------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */
package org.jfree.chart.axis;

import java.io.Serializable;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.internal.Args;

/**
 * The attributes that control the position of the labels for the categories on
 * a {@link CategoryAxis}. Instances of this class are immutable and other
 * JFreeChart classes rely upon this.
 */
public class CategoryLabelPosition implements Serializable {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 5168681143844183864L;

    /**
     * The category anchor point.
     */
    private RectangleAnchor categoryAnchor;

    /**
     * The text block anchor.
     */
    private TextBlockAnchor labelAnchor;

    /**
     * The rotation anchor.
     */
    private TextAnchor rotationAnchor;

    /**
     * The rotation angle (in radians).
     */
    private double angle;

    /**
     * The width calculation type.
     */
    private CategoryLabelWidthType widthType;

    /**
     * The maximum label width as a percentage of the category space or the
     * range space.
     */
    private float widthRatio;

    /**
     * Creates a new position record with default settings.
     */
    public CategoryLabelPosition() {
        this(RectangleAnchor.CENTER, TextBlockAnchor.BOTTOM_CENTER, TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
    }

    /**
     * Creates a new category label position record.
     *
     * @param categoryAnchor  the category anchor ({@code null} not
     *                        permitted).
     * @param labelAnchor  the label anchor ({@code null} not permitted).
     */
    public CategoryLabelPosition(RectangleAnchor categoryAnchor, TextBlockAnchor labelAnchor) {
        // argument checking delegated...
        this(categoryAnchor, labelAnchor, TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
    }

    /**
     * Creates a new category label position record.
     *
     * @param categoryAnchor  the category anchor ({@code null} not
     *                        permitted).
     * @param labelAnchor  the label anchor ({@code null} not permitted).
     * @param widthType  the width type ({@code null} not permitted).
     * @param widthRatio  the maximum label width as a percentage (of the
     *                    category space or the range space).
     */
    public CategoryLabelPosition(RectangleAnchor categoryAnchor, TextBlockAnchor labelAnchor, CategoryLabelWidthType widthType, float widthRatio) {
        // argument checking delegated...
        this(categoryAnchor, labelAnchor, TextAnchor.CENTER, 0.0, widthType, widthRatio);
    }

    /**
     * Creates a new position record.  The item label anchor is a point
     * relative to the data item (dot, bar or other visual item) on a chart.
     * The item label is aligned by aligning the text anchor with the item
     * label anchor.
     *
     * @param categoryAnchor  the category anchor ({@code null} not
     *                        permitted).
     * @param labelAnchor  the label anchor ({@code null} not permitted).
     * @param rotationAnchor  the rotation anchor ({@code null} not
     *                        permitted).
     * @param angle  the rotation angle ({@code null} not permitted).
     * @param widthType  the width type ({@code null} not permitted).
     * @param widthRatio  the maximum label width as a percentage (of the
     *                    category space or the range space).
     */
    public CategoryLabelPosition(RectangleAnchor categoryAnchor, TextBlockAnchor labelAnchor, TextAnchor rotationAnchor, double angle, CategoryLabelWidthType widthType, float widthRatio) {
        Args.nullNotPermitted(categoryAnchor, "categoryAnchor");
        Args.nullNotPermitted(labelAnchor, "labelAnchor");
        Args.nullNotPermitted(rotationAnchor, "rotationAnchor");
        Args.nullNotPermitted(widthType, "widthType");
        this.categoryAnchor = categoryAnchor;
        this.labelAnchor = labelAnchor;
        this.rotationAnchor = rotationAnchor;
        this.angle = angle;
        this.widthType = widthType;
        this.widthRatio = widthRatio;
    }

    /**
     * Returns the item label anchor.
     *
     * @return The item label anchor (never {@code null}).
     */
    public RectangleAnchor getCategoryAnchor();

    /**
     * Returns the text block anchor.
     *
     * @return The text block anchor (never {@code null}).
     */
    public TextBlockAnchor getLabelAnchor();

    /**
     * Returns the rotation anchor point.
     *
     * @return The rotation anchor point (never {@code null}).
     */
    public TextAnchor getRotationAnchor();

    /**
     * Returns the angle of rotation for the label.
     *
     * @return The angle (in radians).
     */
    public double getAngle();

    /**
     * Returns the width calculation type.
     *
     * @return The width calculation type (never {@code null}).
     */
    public CategoryLabelWidthType getWidthType();

    /**
     * Returns the ratio used to calculate the maximum category label width.
     *
     * @return The ratio.
     */
    public float getWidthRatio();

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj);

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode();
}

Please generate a more understandable version of the test code using the above guidelines.