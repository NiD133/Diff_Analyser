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

public class CategoryLabelPosition_ESTestTest20 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition();
        categoryLabelPosition0.getLabelAnchor();
        assertEquals(0.0, categoryLabelPosition0.getAngle(), 0.01);
        assertEquals(0.95F, categoryLabelPosition0.getWidthRatio(), 0.01F);
    }
}
