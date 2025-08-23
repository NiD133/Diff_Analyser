package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.runner.RunWith;

public class Tokeniser_ESTestTest15 extends Tokeniser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        xmlTreeBuilder0.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser0 = new Tokeniser(xmlTreeBuilder0);
        // Undeclared exception!
        try {
            tokeniser0.emit('C');
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }
}
