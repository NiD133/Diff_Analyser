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

public class LocationTextExtractionStrategy_ESTestTest14 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Vector vector0 = new Vector(92.34146F, 92.34146F, 92.34146F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, 2);
        float float0 = locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelEnd();
        assertEquals(92.34146F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelStart(), 0.01F);
        assertEquals((-92), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distPerpendicular());
        assertEquals(2.0F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.getCharSpaceWidth(), 0.01F);
        assertEquals(92.34146F, float0, 0.01F);
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.orientationMagnitude());
    }
}
