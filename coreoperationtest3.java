package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoreOperationTestTest3 extends AbstractJXPathTest {

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
    void testNan() {
        assertXPathValue(context, "$nan > $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= $nan and $nan <= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan > 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 0 and $nan <= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan > 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 1 and $nan <= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != 1", Boolean.FALSE, Boolean.class);
    }
}
