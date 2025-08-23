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

public class CustomCategoryURLGenerator_ESTestTest17 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        CustomCategoryURLGenerator customCategoryURLGenerator0 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator customCategoryURLGenerator1 = new CustomCategoryURLGenerator();
        assertTrue(customCategoryURLGenerator1.equals((Object) customCategoryURLGenerator0));
        Vector<String> vector0 = new Vector<String>();
        vector0.add("N8)j");
        customCategoryURLGenerator1.addURLSeries(vector0);
        Stack<String> stack0 = new Stack<String>();
        stack0.add((String) null);
        customCategoryURLGenerator0.addURLSeries(stack0);
        boolean boolean0 = customCategoryURLGenerator0.equals(customCategoryURLGenerator1);
        assertFalse(customCategoryURLGenerator1.equals((Object) customCategoryURLGenerator0));
        assertFalse(boolean0);
    }
}
