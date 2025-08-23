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

public class CategoryLabelPosition_ESTestTest3 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.BOTTOM;
        TextBlockAnchor textBlockAnchor0 = TextBlockAnchor.TOP_LEFT;
        TextAnchor textAnchor0 = TextAnchor.BASELINE_RIGHT;
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.CATEGORY;
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, textAnchor0, (-1.0), categoryLabelWidthType0, (-1989.5195F));
        float float0 = categoryLabelPosition0.getWidthRatio();
        assertEquals((-1989.5195F), float0, 0.01F);
        assertEquals((-1.0), categoryLabelPosition0.getAngle(), 0.01);
    }
}
