package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class Elements_ESTestTest41 extends Elements_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test040() throws Throwable {
        Document document0 = new Document("b*LY=0yr*g]q30");
        Elements elements0 = document0.getAllElements();
        NodeFilter nodeFilter0 = mock(NodeFilter.class, new ViolatedAssumptionAnswer());
        doReturn((NodeFilter.FilterResult) null).when(nodeFilter0).head(any(org.jsoup.nodes.Node.class), anyInt());
        Elements elements1 = elements0.filter(nodeFilter0);
        assertSame(elements0, elements1);
    }
}
