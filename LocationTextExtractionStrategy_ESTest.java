package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfOCProperties;
import com.itextpdf.text.pdf.PdfSigLockDictionary;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.GraphicsState;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.InlineImageInfo;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.MarkedContentInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collection;
import java.util.LinkedList;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class LocationTextExtractionStrategy_ESTest extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testDistanceFromEndOf() throws Throwable {
        Vector startVector = new Vector(0.0F, -2030.0F, 0.0F);
        LocationTextExtractionStrategy.TextChunk chunk1 = new LocationTextExtractionStrategy.TextChunk("", startVector, startVector, 0.0F);
        LocationTextExtractionStrategy.TextChunk chunk2 = new LocationTextExtractionStrategy.TextChunk("Lm", startVector, startVector, 2);

        float distance = chunk1.distanceFromEndOf(chunk2);

        assertEquals(0.0F, distance, 0.01F);
        assertEquals(2.0F, chunk2.getCharSpaceWidth(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testCompareTo() throws Throwable {
        Vector vectorA = new Vector(-555.0505F, -555.0505F, -555.0505F);
        Vector vectorB = new Vector(0, -3318.408F, 1);

        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationA = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vectorA, vectorA, -3318.408F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationB = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vectorA, vectorB, 0.0F);

        int comparisonResult = locationA.compareTo(locationB);

        assertEquals(0.0F, locationB.getCharSpaceWidth(), 0.01F);
        assertEquals(-1372, locationB.orientationMagnitude());
        assertEquals(3192.0986F, locationB.distParallelEnd(), 0.01F);
        assertEquals(641, locationB.distPerpendicular());
        assertEquals(319.22217F, locationB.distParallelStart(), 0.01F);
        assertEquals(1, comparisonResult);
        assertEquals(555, locationA.distPerpendicular());
    }

    @Test(timeout = 4000)
    public void testIsAtWordBoundary() throws Throwable {
        GraphicsState graphicsState = new GraphicsState();
        byte[] imageBytes = new byte[3];
        PdfSigLockDictionary.LockAction lockAction = PdfSigLockDictionary.LockAction.EXCLUDE;
        String[] emptyStringArray = new String[0];
        PdfSigLockDictionary lockDictionary = new PdfSigLockDictionary(lockAction, emptyStringArray);
        InlineImageInfo inlineImageInfo = new InlineImageInfo(imageBytes, lockDictionary);
        LinkedList<MarkedContentInfo> markedContentInfos = new LinkedList<>();

        ImageRenderInfo imageRenderInfo = ImageRenderInfo.createForEmbeddedImage(graphicsState, inlineImageInfo, lockDictionary, markedContentInfos);
        Vector startVector = imageRenderInfo.getStartPoint();
        Vector endVector = startVector.multiply(1163.3555F);

        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationA = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(startVector, endVector, 7);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationB = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(startVector, startVector, 4);

        boolean isAtWordBoundary = locationB.isAtWordBoundary(locationA);

        assertEquals(1163.3555F, locationA.distParallelEnd(), 0.01F);
        assertFalse(isAtWordBoundary);
        assertEquals(0, locationB.distPerpendicular());
        assertEquals(0.0F, locationB.distParallelStart(), 0.01F);
        assertEquals(0, locationB.orientationMagnitude());
    }

    @Test(timeout = 4000)
    public void testSameLine() throws Throwable {
        Vector vectorA = new Vector(-53.7697F, 1000.0F, 1000.0F);
        Vector vectorB = new Vector(1.089F, -53.7697F, 1.089F);

        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationA = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vectorA, vectorA, 0);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationB = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vectorB, vectorB, 0);

        boolean sameLine = locationA.sameLine(locationB);

        assertEquals(0, locationB.orientationMagnitude());
        assertEquals(0.0F, locationB.getCharSpaceWidth(), 0.01F);
        assertEquals(53, locationB.distPerpendicular());
        assertFalse(sameLine);
        assertEquals(1.089F, locationB.distParallelStart(), 0.01F);
        assertEquals(1.089F, locationB.distParallelEnd(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNullTextRenderInfo() throws Throwable {
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        try {
            strategy.renderText(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetResultantTextWithNullFilter() throws Throwable {
        PdfDate pdfDate = new PdfDate();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix(8, 2);
        PdfOCProperties pdfOCProperties = new PdfOCProperties();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfOCProperties);
        LinkedList<MarkedContentInfo> markedContentInfos = new LinkedList<>();
        graphicsState.font = font;
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfDate, graphicsState, matrix, markedContentInfos);

        LocationTextExtractionStrategy.TextChunkLocationStrategy locationStrategy = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        doReturn(null, null).when(locationStrategy).createLocation(any(TextRenderInfo.class), any(LineSegment.class));

        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy(locationStrategy);
        strategy.renderText(textRenderInfo);
        strategy.renderText(textRenderInfo);

        try {
            strategy.getResultantText((LocationTextExtractionStrategy.TextChunkFilter) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy$TextChunk", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetResultantText() throws Throwable {
        PdfDate pdfDate = new PdfDate();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix(8, 2);
        PdfOCProperties pdfOCProperties = new PdfOCProperties();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfOCProperties);
        LinkedList<MarkedContentInfo> markedContentInfos = new LinkedList<>();
        graphicsState.font = font;
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfDate, graphicsState, matrix, markedContentInfos);

        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        strategy.renderText(textRenderInfo);
        strategy.renderText(textRenderInfo);

        String resultantText = strategy.getResultantText();
        assertEquals("", resultantText);
    }
}