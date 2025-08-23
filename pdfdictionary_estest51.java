package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.collection.PdfCollectionField;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.LinkedHashMap;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class PdfDictionary_ESTestTest51 extends PdfDictionary_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        FdfWriter.Wrt fdfWriter_Wrt0 = new FdfWriter.Wrt((OutputStream) null, (FdfWriter) null);
        BaseColor baseColor0 = BaseColor.MAGENTA;
        PdfShading pdfShading0 = PdfShading.simpleRadial((PdfWriter) fdfWriter_Wrt0, (float) 512, (float) 1, (float) 32, (float) 8, (float) 8192, 663.0F, baseColor0, baseColor0);
        assertFalse(pdfShading0.isAntiAlias());
    }
}
