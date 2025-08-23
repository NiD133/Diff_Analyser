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

public class CategoryLabelPosition_ESTestTest26 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition();
        double double0 = categoryLabelPosition0.getAngle();
        assertEquals(0.95F, categoryLabelPosition0.getWidthRatio(), 0.01F);
        assertEquals(0.0, double0, 0.01);
    }
}
