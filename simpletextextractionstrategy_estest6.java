package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfSigLockDictionary;
import com.itextpdf.text.pdf.PdfString;
import java.nio.CharBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javax.swing.text.Segment;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SimpleTextExtractionStrategy_ESTestTest6 extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        SimpleTextExtractionStrategy simpleTextExtractionStrategy0 = new SimpleTextExtractionStrategy();
        Segment segment0 = new Segment((char[]) null, 2087, 2087);
        // Undeclared exception!
        try {
            simpleTextExtractionStrategy0.appendTextChunk(segment0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("javax.swing.text.Segment", e);
        }
    }
}
