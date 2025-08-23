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

public class CategoryLabelPosition_ESTestTest19 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition();
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor textBlockAnchor0 = TextBlockAnchor.CENTER_RIGHT;
        CategoryLabelPosition categoryLabelPosition1 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0);
        boolean boolean0 = categoryLabelPosition1.equals(categoryLabelPosition0);
        assertEquals(0.95F, categoryLabelPosition1.getWidthRatio(), 0.01F);
        assertFalse(boolean0);
        assertEquals(0.0, categoryLabelPosition1.getAngle(), 0.01);
    }
}
