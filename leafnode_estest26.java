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

public class LeafNode_ESTestTest26 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("-/9fsuZ");
        cDataNode0.value = (Object) cDataNode0;
        // Undeclared exception!
        try {
            cDataNode0.attributes();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // org.jsoup.nodes.CDataNode cannot be cast to java.lang.String
            //
            verifyException("org.jsoup.nodes.LeafNode", e);
        }
    }
}
