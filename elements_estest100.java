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

public class Elements_ESTestTest100 extends Elements_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test099() throws Throwable {
        Document document0 = Document.createShell("\n");
        Elements elements0 = document0.getAllElements();
        Document.OutputSettings.Syntax document_OutputSettings_Syntax0 = Document.OutputSettings.Syntax.xml;
        Predicate<Object> predicate0 = Predicate.isEqual((Object) document_OutputSettings_Syntax0);
        boolean boolean0 = elements0.removeIf(predicate0);
        assertFalse(boolean0);
    }
}
