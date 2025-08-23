package org.jfree.chart.urls;

import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomCategoryURLGeneratorTestTest5 {

    /**
     * Some checks for the addURLSeries() method.
     */
    @Test
    public void testAddURLSeries() {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        // you can add a null list - it would have been better if this
        // required EMPTY_LIST
        g1.addURLSeries(null);
        assertEquals(1, g1.getListCount());
        assertEquals(0, g1.getURLCount(0));
        List<String> list1 = new ArrayList<>();
        list1.add("URL1");
        g1.addURLSeries(list1);
        assertEquals(2, g1.getListCount());
        assertEquals(0, g1.getURLCount(0));
        assertEquals(1, g1.getURLCount(1));
        assertEquals("URL1", g1.getURL(1, 0));
        // if we modify the original list, it's best if the URL generator is
        // not affected
        list1.clear();
        assertEquals("URL1", g1.getURL(1, 0));
    }
}
