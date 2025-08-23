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

public class CategoryLabelPosition_ESTestTest11 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.BOTTOM_LEFT;
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.RANGE;
        CategoryLabelPosition categoryLabelPosition0 = null;
        try {
            categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, (TextBlockAnchor) null, categoryLabelWidthType0, 2093.0F);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'labelAnchor' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
