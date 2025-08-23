package org.jfree.chart.urls;

import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomCategoryURLGeneratorTestTest1 {

    /**
     * Some checks for the equals() method.
     */
    @Test
    public void testEquals() {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();
        assertEquals(g1, g2);
        List<String> u1 = new ArrayList<>();
        u1.add("URL A1");
        u1.add("URL A2");
        u1.add("URL A3");
        g1.addURLSeries(u1);
        assertNotEquals(g1, g2);
        List<String> u2 = new ArrayList<>();
        u2.add("URL A1");
        u2.add("URL A2");
        u2.add("URL A3");
        g2.addURLSeries(u2);
        assertEquals(g1, g2);
    }
}
