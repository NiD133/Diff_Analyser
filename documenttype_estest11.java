package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.junit.runner.RunWith;

public class DocumentType_ESTestTest11 extends DocumentType_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        DocumentType documentType0 = new DocumentType("C0pe>FSNio5LQF[+", "C0pe>FSNio5LQF[+", "org.jsoup.helper.Validate");
        StringBuffer stringBuffer0 = new StringBuffer((CharSequence) "C0pe>FSNio5LQF[+");
        QuietAppendable quietAppendable0 = QuietAppendable.wrap(stringBuffer0);
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        Document.OutputSettings.Syntax document_OutputSettings_Syntax0 = Document.OutputSettings.Syntax.xml;
        Document.OutputSettings document_OutputSettings1 = document_OutputSettings0.syntax(document_OutputSettings_Syntax0);
        Printer.Pretty printer_Pretty0 = new Printer.Pretty(documentType0, quietAppendable0, document_OutputSettings1);
        documentType0.traverse(printer_Pretty0);
        assertEquals("C0pe>FSNio5LQF[+<!DOCTYPE C0pe>FSNio5LQF[+ PUBLIC \"C0pe>FSNio5LQF[+\" \"org.jsoup.helper.Validate\">", stringBuffer0.toString());
        assertEquals(97, stringBuffer0.length());
    }
}
