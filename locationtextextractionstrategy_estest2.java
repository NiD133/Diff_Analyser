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

public class LocationTextExtractionStrategy_ESTestTest2 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Vector vector0 = new Vector((-555.0505F), (-555.0505F), (-555.0505F));
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp0 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector0, (-3318.408F));
        Vector vector1 = new Vector(0, (-3318.408F), 1);
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp locationTextExtractionStrategy_TextChunkLocationDefaultImp1 = new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(vector0, vector1, 0.0F);
        int int0 = locationTextExtractionStrategy_TextChunkLocationDefaultImp0.compareTo((LocationTextExtractionStrategy.TextChunkLocation) locationTextExtractionStrategy_TextChunkLocationDefaultImp1);
        assertEquals(0.0F, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.getCharSpaceWidth(), 0.01F);
        assertEquals((-1372), locationTextExtractionStrategy_TextChunkLocationDefaultImp1.orientationMagnitude());
        assertEquals(3192.0986F, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distParallelEnd(), 0.01F);
        assertEquals(641, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distPerpendicular());
        assertEquals(319.22217F, locationTextExtractionStrategy_TextChunkLocationDefaultImp1.distParallelStart(), 0.01F);
        assertEquals(1, int0);
        assertEquals(555, locationTextExtractionStrategy_TextChunkLocationDefaultImp0.distPerpendicular());
    }
}
