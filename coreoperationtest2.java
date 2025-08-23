package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoreOperationTestTest2 extends AbstractJXPathTest {

    private JXPathContext context;

    @Override
    @BeforeEach
    public void setUp() {
        if (context == null) {
            context = JXPathContext.newContext(null);
            final Variables vars = context.getVariables();
            vars.declareVariable("integer", Integer.valueOf(1));
            vars.declareVariable("array", new double[] { 0.25, 0.5, 0.75 });
            vars.declareVariable("nan", Double.valueOf(Double.NaN));
        }
    }

    @Test
    void testInfoSetTypes() {
        // Numbers
        assertXPathValue(context, "1", Double.valueOf(1.0));
        assertXPathPointer(context, "1", "1");
        assertXPathValueIterator(context, "1", list(Double.valueOf(1.0)));
        assertXPathPointerIterator(context, "1", list("1"));
        assertXPathValue(context, "-1", Double.valueOf(-1.0));
        assertXPathValue(context, "2 + 2", Double.valueOf(4.0));
        assertXPathValue(context, "3 - 2", Double.valueOf(1.0));
        assertXPathValue(context, "1 + 2 + 3 - 4 + 5", Double.valueOf(7.0));
        assertXPathValue(context, "3 * 2", Double.valueOf(3.0 * 2.0));
        assertXPathValue(context, "3 div 2", Double.valueOf(3.0 / 2.0));
        assertXPathValue(context, "5 mod 2", Double.valueOf(1.0));
        // This test produces a different result with Xalan?
        assertXPathValue(context, "5.9 mod 2.1", Double.valueOf(1.0));
        assertXPathValue(context, "5 mod -2", Double.valueOf(1.0));
        assertXPathValue(context, "-5 mod 2", Double.valueOf(-1.0));
        assertXPathValue(context, "-5 mod -2", Double.valueOf(-1.0));
        assertXPathValue(context, "1 < 2", Boolean.TRUE);
        assertXPathValue(context, "1 > 2", Boolean.FALSE);
        assertXPathValue(context, "1 <= 1", Boolean.TRUE);
        assertXPathValue(context, "1 >= 2", Boolean.FALSE);
        assertXPathValue(context, "3 > 2 > 1", Boolean.FALSE);
        assertXPathValue(context, "3 > 2 and 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 > 2 and 2 < 1", Boolean.FALSE);
        assertXPathValue(context, "3 < 2 or 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 < 2 or 2 < 1", Boolean.FALSE);
        assertXPathValue(context, "1 = 1", Boolean.TRUE);
        assertXPathValue(context, "1 = '1'", Boolean.TRUE);
        assertXPathValue(context, "1 > 2 = 2 > 3", Boolean.TRUE);
        assertXPathValue(context, "1 > 2 = 0", Boolean.TRUE);
        assertXPathValue(context, "1 = 2", Boolean.FALSE);
        assertXPathValue(context, "$integer", Double.valueOf(1), Double.class);
        assertXPathValue(context, "2 + 3", "5.0", String.class);
        assertXPathValue(context, "2 + 3", Boolean.TRUE, boolean.class);
        assertXPathValue(context, "'true'", Boolean.TRUE, Boolean.class);
    }
}
