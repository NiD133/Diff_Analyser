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

public class LeafNode_ESTestTest6 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("Gyb$AWbT${");
        cDataNode0.attr("PUBLIC", (String) null);
        boolean boolean0 = cDataNode0.hasAttributes();
        assertTrue(boolean0);
    }
}
