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

public class CategoryLabelPosition_ESTestTest10 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        RectangleAnchor rectangleAnchor0 = RectangleAnchor.TOP_RIGHT;
        TextAnchor textAnchor0 = TextAnchor.CENTER;
        CategoryLabelWidthType categoryLabelWidthType0 = CategoryLabelWidthType.CATEGORY;
        CategoryLabelPosition categoryLabelPosition0 = null;
        try {
            categoryLabelPosition0 = new CategoryLabelPosition(rectangleAnchor0, (TextBlockAnchor) null, textAnchor0, 1000.4695532, categoryLabelWidthType0, (-1517.6609F));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'labelAnchor' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
