package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TextRenderInfo_ESTestTest42 extends TextRenderInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        GraphicsState graphicsState0 = new GraphicsState();
        PdfGState pdfGState0 = new PdfGState();
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfGState0);
        graphicsState0.font = cMapAwareDocumentFont0;
        Matrix matrix0 = graphicsState0.getCtm();
        Stack<MarkedContentInfo> stack0 = new Stack<MarkedContentInfo>();
        PdfString pdfString0 = new PdfString("MacRoman", "Cp1250");
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfString0, graphicsState0, matrix0, stack0);
        // Undeclared exception!
        try {
            textRenderInfo0.getDescentLine();
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            //
            // java.nio.ByteBuffer.rewind()Ljava/nio/ByteBuffer;
            //
            verifyException("com.itextpdf.text.pdf.PdfEncodings", e);
        }
    }
}
