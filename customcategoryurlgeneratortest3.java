package org.jfree.chart.urls;

import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomCategoryURLGeneratorTestTest3 {

    /**
     * Checks that the class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
        assertTrue(g1 instanceof PublicCloneable);
    }
}
