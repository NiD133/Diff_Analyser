package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;

public class XmlTreeBuilder_ESTestTest40 extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        xmlTreeBuilder0.parse(",Ix<T(pi#>?mbGzH", ",Ix<T(pi#>?mbGzH");
        Token.StartTag token_StartTag0 = new Token.StartTag(xmlTreeBuilder0);
        CDataNode cDataNode0 = new CDataNode("1'm<`CcR11r1m =@q");
        Attributes attributes0 = cDataNode0.attributes();
        attributes0.add("xmlns", "1'm<`CcR11r1m =@q");
        Token.StartTag token_StartTag1 = token_StartTag0.nameAttr("X7,c,ddhqJ", attributes0);
        // Undeclared exception!
        try {
            xmlTreeBuilder0.process(token_StartTag1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }
}