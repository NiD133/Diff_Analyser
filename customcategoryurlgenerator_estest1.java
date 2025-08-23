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

public class CustomCategoryURLGenerator_ESTestTest1 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        CustomCategoryURLGenerator customCategoryURLGenerator0 = new CustomCategoryURLGenerator();
        Vector<String> vector0 = new Vector<String>();
        vector0.add(",ql\"(}:Q!;Ew");
        vector0.add(",ql\"(}:Q!;Ew");
        customCategoryURLGenerator0.addURLSeries(vector0);
        Object object0 = customCategoryURLGenerator0.clone();
        boolean boolean0 = customCategoryURLGenerator0.equals(object0);
        assertTrue(boolean0);
        assertNotSame(object0, customCategoryURLGenerator0);
    }
}