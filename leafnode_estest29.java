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

public class LeafNode_ESTestTest29 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        CDataNode cDataNode0 = new CDataNode(">eaHxh");
        Object object0 = new Object();
        cDataNode0.value = object0;
        // Undeclared exception!
        try {
            cDataNode0.attr(">eaHxh", ">eaHxh");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // java.lang.Object cannot be cast to java.lang.String
            //
            verifyException("org.jsoup.nodes.LeafNode", e);
        }
    }
}
