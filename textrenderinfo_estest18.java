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

public class TextRenderInfo_ESTestTest18 extends TextRenderInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        PdfDate pdfDate0 = new PdfDate();
        GraphicsState graphicsState0 = new GraphicsState();
        PdfGState pdfGState0 = new PdfGState();
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfGState0);
        graphicsState0.font = cMapAwareDocumentFont0;
        graphicsState0.fontSize = (-744.2958F);
        Stack<MarkedContentInfo> stack0 = new Stack<MarkedContentInfo>();
        Matrix matrix0 = new Matrix(2, 4);
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfDate0, graphicsState0, matrix0, stack0);
        float float0 = textRenderInfo0.getSingleSpaceWidth();
        assertEquals(744.2958F, float0, 0.01F);
    }
}
