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

public class LeafNode_ESTestTest10 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        XmlDeclaration xmlDeclaration0 = new XmlDeclaration("<eaHch", false);
        xmlDeclaration0.siblingIndex = 1574;
        Node node0 = xmlDeclaration0.empty();
        assertSame(xmlDeclaration0, node0);
    }
}
