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

public class CategoryLabelPosition_ESTestTest13 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.TOP;
        TextBlockAnchor textBlockAnchor0 = TextBlockAnchor.CENTER_RIGHT;
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0);
        TextAnchor textAnchor0 = TextAnchor.CENTER;
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.CATEGORY;
        CategoryLabelPosition categoryLabelPosition1 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, textAnchor0, (-447.68625781958866), categoryLabelWidthType0, (-1488.0F));
        boolean boolean0 = categoryLabelPosition0.equals(categoryLabelPosition1);
        assertFalse(boolean0);
        assertEquals((-1488.0F), categoryLabelPosition1.getWidthRatio(), 0.01F);
        assertEquals((-447.68625781958866), categoryLabelPosition1.getAngle(), 0.01);
    }
}
