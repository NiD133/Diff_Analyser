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

public class CustomCategoryURLGenerator_ESTestTest5 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        CustomCategoryURLGenerator customCategoryURLGenerator0 = new CustomCategoryURLGenerator();
        ArrayList<String> arrayList0 = new ArrayList<String>();
        customCategoryURLGenerator0.addURLSeries(arrayList0);
        String string0 = customCategoryURLGenerator0.getURL(0, 2440);
        assertNull(string0);
    }
}
