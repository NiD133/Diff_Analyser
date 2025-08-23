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
import java.nio.charset.IllegalCharsetNameException;
import java.util.Collection;
import java.util.LinkedList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class LocationTextExtractionStrategy_ESTestTest9 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Vector vector0 = new Vector(1518.243F, 1518.243F, (-2451.2717F));
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, (-1477.5446F));
        locationTextExtractionStrategy_TextChunkLocationDefaultImp0.getStartLocation();
        assertEquals(1518.243F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelStart(), 0.01F);
        assertEquals(1518.243F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelEnd(), 0.01F);
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.orientationMagnitude());
        assertEquals((-1477.5446F), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.getCharSpaceWidth(), 0.01F);
        assertEquals((-1518), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distPerpendicular());
    }
}
