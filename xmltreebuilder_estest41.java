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

public class XmlTreeBuilder_ESTestTest41 extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        xmlTreeBuilder0.parse("http://www.w3.org/XML/1998/namespace", "http://www.w3.org/XML/1998/namespace");
        Tokeniser tokeniser0 = new Tokeniser(xmlTreeBuilder0);
        Token.StartTag token_StartTag0 = tokeniser0.startPending;
        Attributes attributes0 = new Attributes();
        attributes0.add("http://www.w3.org/XML/1998/namespace", "}5ov4zKP6");
        token_StartTag0.nameAttr("numeric referencD with no nu)erals", attributes0);
        // Undeclared exception!
        try {
            xmlTreeBuilder0.process(token_StartTag0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }
}
