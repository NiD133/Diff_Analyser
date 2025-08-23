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

public class CustomCategoryURLGenerator_ESTestTest7 extends CustomCategoryURLGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        CustomCategoryURLGenerator customCategoryURLGenerator0 = new CustomCategoryURLGenerator();
        List<String> list0 = List.of("2p8~6Bu{q8c&Ro)", "2p8~6Bu{q8c&Ro)", "2p8~6Bu{q8c&Ro)", "2p8~6Bu{q8c&Ro)", "", "");
        customCategoryURLGenerator0.addURLSeries(list0);
        int int0 = customCategoryURLGenerator0.getURLCount(0);
        assertEquals(6, int0);
    }
}
