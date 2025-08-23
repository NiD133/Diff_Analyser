package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.junit.runner.RunWith;

public class CustomCategoryURLGenerator_ESTestTest25 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        CustomCategoryURLGenerator customCategoryURLGenerator0 = new CustomCategoryURLGenerator();
        DefaultCategoryDataset<String, String> defaultCategoryDataset0 = new DefaultCategoryDataset<String, String>();
        String string0 = customCategoryURLGenerator0.generateURL(defaultCategoryDataset0, 860, 0);
        assertNull(string0);
    }
}
