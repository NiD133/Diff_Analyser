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

public class LeafNode_ESTestTest17 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        TextNode textNode0 = new TextNode("gMz?D>{rCwB%:");
        String string0 = textNode0.coreValue();
        assertEquals("gMz?D>{rCwB%:", string0);
    }
}
