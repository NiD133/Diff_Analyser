package com.itextpdf.text.xml.xmp;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.xmp.XMPMeta;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import javax.swing.DebugGraphics;
import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class XmpWriter_ESTestTest17 extends XmpWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        XmpWriter xmpWriter0 = new XmpWriter((OutputStream) null);
        xmpWriter0.xmpMeta = null;
        // Undeclared exception!
        try {
            xmpWriter0.addRdfDescription("|A", "|A");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.itextpdf.text.xml.xmp.XmpWriter", e);
        }
    }
}