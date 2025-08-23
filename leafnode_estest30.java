package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class LeafNode_ESTestTest30 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("Rcdata");
        Node node0 = cDataNode0.attr("Rcdata", "Rcdata");
        // Undeclared exception!
        try {
            node0.attr((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Object must not be null
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
