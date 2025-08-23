package org.jfree.chart.urls;

import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomCategoryURLGeneratorTestTest4 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        List<String> u1 = new ArrayList<>();
        u1.add("URL A1");
        u1.add("URL A2");
        u1.add("URL A3");
        List<String> u2 = new ArrayList<>();
        u2.add("URL B1");
        u2.add("URL B2");
        u2.add("URL B3");
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        g1.addURLSeries(u1);
        g1.addURLSeries(u2);
        CustomCategoryURLGenerator g2 = TestUtils.serialised(g1);
        assertEquals(g1, g2);
    }
}
