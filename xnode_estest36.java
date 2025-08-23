package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;
import javax.imageio.metadata.IIOMetadataNode;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ext.DefaultHandler2;

public class XNode_ESTestTest36 extends XNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test035() throws Throwable {
        IIOMetadataNode iIOMetadataNode0 = new IIOMetadataNode(" />\n");
        XPathParser xPathParser0 = new XPathParser((Document) null, true);
        Properties properties0 = new Properties();
        XNode xNode0 = new XNode(xPathParser0, iIOMetadataNode0, properties0);
        Double double0 = new Double(768.82985);
        Double double1 = xNode0.getDoubleAttribute("(z|PWb^", double0);
        assertEquals(768.82985, (double) double1, 0.01);
    }
}
