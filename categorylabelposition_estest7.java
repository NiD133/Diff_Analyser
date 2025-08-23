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

public class CategoryLabelPosition_ESTestTest7 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.RIGHT;
        TextBlockAnchor textBlockAnchor0 = TextBlockAnchor.BOTTOM_CENTER;
        TextAnchor textAnchor0 = TextAnchor.BASELINE_CENTER;
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.RANGE;
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, textAnchor0, 0.0, categoryLabelWidthType0, (-985.5677F));
        TextAnchor textAnchor1 = categoryLabelPosition0.getRotationAnchor();
        CategoryLabelPosition categoryLabelPosition1 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, textAnchor1, 986.267245817, categoryLabelWidthType0, (-985.5677F));
        boolean boolean0 = categoryLabelPosition0.equals(categoryLabelPosition1);
        assertFalse(categoryLabelPosition1.equals((Object) categoryLabelPosition0));
        assertFalse(boolean0);
        assertEquals((-985.5677F), categoryLabelPosition1.getWidthRatio(), 0.01F);
        assertEquals(986.267245817, categoryLabelPosition1.getAngle(), 0.01);
    }
}
