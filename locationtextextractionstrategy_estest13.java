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

public class LocationTextExtractionStrategy_ESTestTest13 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Vector vector0 = new Vector(3501.2126F, 3501.2126F, 3501.2126F);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, (-408.0F));
        float float0 = locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelStart();
        assertEquals(3501.2126F, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distParallelEnd(), 0.01F);
        assertEquals(3501.2126F, float0, 0.01F);
        assertEquals((-3501), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distPerpendicular());
        assertEquals((-408.0F), locationTextExtractionStrategy_TextChunkLocationDefaultImp0.getCharSpaceWidth(), 0.01F);
        assertEquals(0, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.orientationMagnitude());
    }
}
