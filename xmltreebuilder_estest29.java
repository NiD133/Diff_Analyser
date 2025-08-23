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

public class XmlTreeBuilder_ESTestTest29 extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        // Undeclared exception!
        try {
            xmlTreeBuilder0.insertElementFor((Token.StartTag) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.HashMap", e);
        }
    }
}
