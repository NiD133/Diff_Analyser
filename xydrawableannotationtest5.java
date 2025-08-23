package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.TestUtils;
import org.jfree.chart.Drawable;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYDrawableAnnotationTestTest5 {

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
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        XYDrawableAnnotation a1 = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new TestDrawable());
        XYDrawableAnnotation a2 = TestUtils.serialised(a1);
        assertEquals(a1, a2);
    }
}
