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

public class LocationTextExtractionStrategy_ESTestTest6 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Vector vector0 = new Vector(905.5172F, 905.5172F, 905.5172F);
        Vector vector1 = new Vector(0.0F, (-1162.3555F), 1.0F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector1, vector1, (-2442.038F));
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp1 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector1, vector0, (-1906.0F));
        boolean boolean0 = locationTextExtractionStrategy_TextChunkLocationDefaultImp0.sameLine(locationTextExtractionStrategy_TextChunkLocationDefaultImp1);
        assertEquals(1162, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distPerpendicular());
        assertEquals(432, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distPerpendicular());
        assertEquals(1158, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.orientationMagnitude());
        assertEquals(1443.9287F, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distParallelEnd(), 0.01F);
        assertEquals((-1906.0F), locationTextExtractionStrategy_TextChunkLocationDefaultImp1.getCharSpaceWidth(), 0.01F);
        assertFalse(boolean0);
        assertEquals((-987.98627F), locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distParallelStart(), 0.01F);
    }
}
