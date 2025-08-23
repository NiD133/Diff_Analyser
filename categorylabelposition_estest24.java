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

public class CategoryLabelPosition_ESTestTest24 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor textBlockAnchor0 = TextBlockAnchor.CENTER_RIGHT;
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0);
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.CATEGORY;
        CategoryLabelPosition categoryLabelPosition1 = new CategoryLabelPosition(rectangleAnchor0, textBlockAnchor0, categoryLabelWidthType0, (-2714.5823F));
        boolean boolean0 = categoryLabelPosition1.equals(categoryLabelPosition0);
        assertEquals((-2714.5823F), categoryLabelPosition1.getWidthRatio(), 0.01F);
        assertFalse(categoryLabelPosition0.equals((Object) categoryLabelPosition1));
        assertEquals(0.0, categoryLabelPosition1.getAngle(), 0.01);
        assertFalse(boolean0);
    }
}
