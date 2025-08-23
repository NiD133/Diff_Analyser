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

public class LocationTextExtractionStrategy_ESTestTest25 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Vector vector0 = new Vector(0.0F, (-2030.0F), 0.0F);
        LocationTextExtractionStrategy.TextChunk locationTextExtractionStrategy_TextChunk0 = new LocationTextExtractionStrategy.TextChunk("", vector0, vector0, 0.0F);
        Vector vector1 = new Vector(0.0F, 0.0F, (-2030.0F));
        LocationTextExtractionStrategy.TextChunk locationTextExtractionStrategy_TextChunk1 = new LocationTextExtractionStrategy.TextChunk("Lm", vector1, vector1, 2);
        int int0 = locationTextExtractionStrategy_TextChunk0.compareTo(locationTextExtractionStrategy_TextChunk1);
        assertEquals(2.0F, locationTextExtractionStrategy_TextChunk1.getCharSpaceWidth(), 0.01F);
        assertEquals(1, int0);
    }
}
