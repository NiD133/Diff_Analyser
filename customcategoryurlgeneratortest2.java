package org.jfree.chart.urls;

import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomCategoryURLGeneratorTestTest2 {

    /**
     * Confirm that cloning works.
     * @throws java.lang.CloneNotSupportedException
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        List<String> u1 = new ArrayList<>();
        u1.add("URL A1");
        u1.add("URL A2");
        u1.add("URL A3");
        g1.addURLSeries(u1);
        CustomCategoryURLGenerator g2 = CloneUtils.clone(g1);
        assertNotSame(g1, g2);
        assertSame(g1.getClass(), g2.getClass());
        assertEquals(g1, g2);
        // check independence
        List<String> u2 = new ArrayList<>();
        u2.add("URL XXX");
        g1.addURLSeries(u2);
        assertNotEquals(g1, g2);
        g2.addURLSeries(new ArrayList<>(u2));
        assertEquals(g1, g2);
    }
}
