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

public class LocationTextExtractionStrategy_ESTestTest24 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Vector vector0 = new Vector((-2327.809F), (-1220.9F), 0.0F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, 2);
        LocationTextExtractionStrategy.TextChunk locationTextExtractionStrategy_TextChunk0 = new LocationTextExtractionStrategy.TextChunk("com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy$TextChunk", locationTextExtractionStrategy_TextChunkLocationDefaultImp0);
        LocationTextExtractionStrategy.TextChunkLocation locationTextExtractionStrategy_TextChunkLocation0 = locationTextExtractionStrategy_TextChunk0.getLocation();
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocation0.orientationMagnitude());
        assertEquals(2.0F, locationTextExtractionStrategy_TextChunkLocation0.getCharSpaceWidth(), 0.01F);
        assertEquals(1220, locationTextExtractionStrategy_TextChunkLocation0.distPerpendicular());
        assertEquals((-2327.809F), locationTextExtractionStrategy_TextChunkLocation0.distParallelStart(), 0.01F);
        assertEquals((-2327.809F), locationTextExtractionStrategy_TextChunkLocation0.distParallelEnd(), 0.01F);
    }
}
