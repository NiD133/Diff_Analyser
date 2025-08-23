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

public class XmlTreeBuilder_ESTestTest39 extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        xmlTreeBuilder0.parse("xmlns:{2u;3rllkw{s)I", "xmlns:{2u;3rllkw{s)I");
        CDataNode cDataNode0 = new CDataNode("xmlns:{2u;3rllkw{s)I");
        Attributes attributes0 = cDataNode0.attributes();
        Attributes attributes1 = attributes0.add("xmlns:{2u;3rllkw{s)I", "6x J(VBX=|so_zGPsa");
        Tokeniser tokeniser0 = new Tokeniser(xmlTreeBuilder0);
        Token.StartTag token_StartTag0 = tokeniser0.startPending;
        token_StartTag0.nameAttr((String) null, attributes1);
        // Undeclared exception!
        try {
            xmlTreeBuilder0.process(token_StartTag0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // String must not be empty
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
