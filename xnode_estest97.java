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

public class XNode_ESTestTest97 extends XNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test096() throws Throwable {
        IIOMetadataNode iIOMetadataNode0 = new IIOMetadataNode();
        iIOMetadataNode0.setAttribute("2iY", "2iY");
        Properties properties0 = new Properties();
        XPathParser xPathParser0 = new XPathParser((Document) null, false, properties0);
        XNode xNode0 = new XNode(xPathParser0, iIOMetadataNode0, properties0);
        Integer integer0 = new Integer(46);
        // Undeclared exception!
        try {
            xNode0.getIntAttribute("2iY", integer0);
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // For input string: \"2iY\"
            //
            verifyException("java.lang.NumberFormatException", e);
        }
    }
}
