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

public class CustomCategoryURLGenerator_ESTestTest19 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        CustomCategoryURLGenerator customCategoryURLGenerator0 = new CustomCategoryURLGenerator();
        List<String> list0 = List.of("o<]R");
        customCategoryURLGenerator0.addURLSeries(list0);
        CustomCategoryURLGenerator customCategoryURLGenerator1 = new CustomCategoryURLGenerator();
        Stack<String> stack0 = new Stack<String>();
        customCategoryURLGenerator1.addURLSeries(stack0);
        boolean boolean0 = customCategoryURLGenerator1.equals(customCategoryURLGenerator0);
        assertFalse(customCategoryURLGenerator1.equals((Object) customCategoryURLGenerator0));
        assertFalse(customCategoryURLGenerator0.equals((Object) customCategoryURLGenerator1));
        assertFalse(boolean0);
    }
}
