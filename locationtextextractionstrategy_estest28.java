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

public class LocationTextExtractionStrategy_ESTestTest28 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Vector vector0 = new Vector(2.0F, 2.0F, 175.0F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, (-5.186149E-6F));
        Vector vector1 = new Vector(175.0F, 2.0F, 175.0F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp1 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector1, 0);
        boolean boolean0 = locationTextExtractionStrategy_TextChunkLocationDefaultImp0.isAtWordBoundary(locationTextExtractionStrategy_TextChunkLocationDefaultImp1);
        assertTrue(boolean0);
        assertEquals(175.0F, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distParallelEnd(), 0.01F);
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.orientationMagnitude());
        assertEquals(2.0F, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distParallelStart(), 0.01F);
        assertEquals((-2), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distPerpendicular());
    }
}
