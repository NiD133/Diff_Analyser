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

public class Tokeniser_ESTestTest18 extends Tokeniser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        HtmlTreeBuilder htmlTreeBuilder0 = new HtmlTreeBuilder();
        Tokeniser tokeniser0 = null;
        try {
            tokeniser0 = new Tokeniser(htmlTreeBuilder0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }
}
