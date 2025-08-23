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

public class LeafNode_ESTestTest41 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        DataNode dataNode0 = new DataNode("oML5cI2?Z,t2RXsqa");
        DataNode dataNode1 = dataNode0.setWholeData((String) null);
        String string0 = dataNode1.coreValue();
        assertNull(string0);
    }
}
