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

public class CategoryLabelPosition_ESTestTest25 extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        CategoryLabelPosition categoryLabelPosition0 = new CategoryLabelPosition();
        float float0 = categoryLabelPosition0.getWidthRatio();
        assertEquals(0.95F, float0, 0.01F);
        assertEquals(0.0, categoryLabelPosition0.getAngle(), 0.01);
    }
}
