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

public class LeafNode_ESTestTest24 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("org.jsoup.nodes.LeafNode");
        // Undeclared exception!
        try {
            cDataNode0.removeAttr((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Object must not be null
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
