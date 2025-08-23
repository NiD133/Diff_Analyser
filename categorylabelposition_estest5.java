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

public class CategoryLabelPosition_ESTestTest5 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor textBlockAnchor0 = TextBlockAnchor.CENTER_RIGHT;
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.CATEGORY;
        TextAnchor textAnchor0 = TextAnchor.HALF_ASCENT_RIGHT;
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, textAnchor0, 986.267245817, categoryLabelWidthType0, 0.95F);
        categoryLabelPosition0.getRotationAnchor();
        assertEquals(986.267245817, categoryLabelPosition0.getAngle(), 0.01);
        assertEquals(0.95F, categoryLabelPosition0.getWidthRatio(), 0.01F);
    }
}
