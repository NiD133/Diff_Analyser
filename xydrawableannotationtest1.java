package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.TestUtils;
import org.jfree.chart.Drawable;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYDrawableAnnotationTestTest1 {

    static class TestDrawable implements Drawable, Cloneable, Serializable {

        /**
         * Default constructor.
         */
        public TestDrawable() {
        }

        /**
         * Draws something.
         * @param g2  the graphics device.
         * @param area  the area in which to draw.
         */
        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // do nothing
        }

        /**
         * Tests this object for equality with an arbitrary object.
         * @param obj  the object to test against ({@code null} permitted).
         * @return A boolean.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof TestDrawable)) {
                return false;
            }
            return true;
        }

        /**
         * Returns a clone.
         *
         * @return A clone.
         *
         * @throws CloneNotSupportedException if there is a problem cloning.
         */
        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        XYDrawableAnnotation a1 = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new TestDrawable());
        XYDrawableAnnotation a2 = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new TestDrawable());
        assertEquals(a1, a2);
        a1 = new XYDrawableAnnotation(11.0, 20.0, 100.0, 200.0, new TestDrawable());
        assertNotEquals(a1, a2);
        a2 = new XYDrawableAnnotation(11.0, 20.0, 100.0, 200.0, new TestDrawable());
        assertEquals(a1, a2);
        a1 = new XYDrawableAnnotation(11.0, 22.0, 100.0, 200.0, new TestDrawable());
        assertNotEquals(a1, a2);
        a2 = new XYDrawableAnnotation(11.0, 22.0, 100.0, 200.0, new TestDrawable());
        assertEquals(a1, a2);
        a1 = new XYDrawableAnnotation(11.0, 22.0, 101.0, 200.0, new TestDrawable());
        assertNotEquals(a1, a2);
        a2 = new XYDrawableAnnotation(11.0, 22.0, 101.0, 200.0, new TestDrawable());
        assertEquals(a1, a2);
        a1 = new XYDrawableAnnotation(11.0, 22.0, 101.0, 202.0, new TestDrawable());
        assertNotEquals(a1, a2);
        a2 = new XYDrawableAnnotation(11.0, 22.0, 101.0, 202.0, new TestDrawable());
        assertEquals(a1, a2);
        a1 = new XYDrawableAnnotation(11.0, 22.0, 101.0, 202.0, 2.0, new TestDrawable());
        assertNotEquals(a1, a2);
        a2 = new XYDrawableAnnotation(11.0, 22.0, 101.0, 202.0, 2.0, new TestDrawable());
        assertEquals(a1, a2);
    }
}
