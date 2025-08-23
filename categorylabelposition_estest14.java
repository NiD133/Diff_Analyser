package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.runner.RunWith;

public class CategoryLabelPosition_ESTestTest14 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.BOTTOM_RIGHT;
        TextBlockAnchor textBlockAnchor0 = TextBlockAnchor.CENTER_LEFT;
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.RANGE;
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, categoryLabelWidthType0, (-641.25F));
        TextAnchor textAnchor0 = TextAnchor.CENTER_RIGHT;
        CategoryLabelPosition categoryLabelPosition1 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, textAnchor0, (-641.25F), categoryLabelWidthType0, (-641.25F));
        boolean boolean0 = categoryLabelPosition0.equals(categoryLabelPosition1);
        assertEquals((-641.25F), categoryLabelPosition1.getWidthRatio(), 0.01F);
        assertEquals((-641.25), categoryLabelPosition1.getAngle(), 0.01);
        assertFalse(boolean0);
    }
}
