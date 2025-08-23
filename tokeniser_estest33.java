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

public class Tokeniser_ESTestTest33 extends Tokeniser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Document document0 = Parser.parseBodyFragment("C(p;<i>L_mFu^", "C(p;<i>L_mFu^");
        assertEquals("#root", document0.tagName());
    }
}
