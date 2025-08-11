package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.MultiFilteredRenderListener;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MultiFilteredRenderListener_ESTest extends MultiFilteredRenderListener_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAttachRenderListenerWithEmptyFilters() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockStrategy = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy extractionStrategy = new LocationTextExtractionStrategy(mockStrategy);
        RenderFilter[] emptyFilters = new RenderFilter[7];
        
        listener.attachRenderListener(extractionStrategy, emptyFilters);
        listener.endTextBlock();
    }

    @Test(timeout = 4000)
    public void testBeginTextBlockWithNullStrategy() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        RenderFilter[] emptyFilters = new RenderFilter[0];

        listener.attachRenderListener(null, emptyFilters);

        try {
            listener.beginTextBlock();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderImageWithNullImageInfo() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockStrategy = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy extractionStrategy = new LocationTextExtractionStrategy(mockStrategy);
        RenderFilter[] filters = createRegionTextRenderFilters();

        listener.attachRenderListener(extractionStrategy, filters);
        listener.renderImage(null);
    }

    @Test(timeout = 4000)
    public void testRenderImageWithNullImageInfoThrowsException() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockStrategy = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy extractionStrategy = new LocationTextExtractionStrategy(mockStrategy);
        RenderFilter[] filters = new RenderFilter[3];

        listener.attachRenderListener(extractionStrategy, filters);

        try {
            listener.renderImage(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testEndTextBlockWithNullStrategy() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.attachRenderListener(null, null);

        try {
            listener.endTextBlock();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNullTextRenderInfo() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy extractionStrategy = new LocationTextExtractionStrategy();
        RenderFilter[] emptyFilters = new RenderFilter[0];

        listener.attachRenderListener(extractionStrategy, emptyFilters);

        try {
            listener.renderText(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNullStrategy() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        RenderFilter[] singleFilter = new RenderFilter[1];

        listener.attachRenderListener(null, singleFilter);

        try {
            listener.renderText(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNullTextRenderInfoWithoutException() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.renderText(null);
    }

    @Test(timeout = 4000)
    public void testBeginTextBlockWithoutException() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.beginTextBlock();
    }

    @Test(timeout = 4000)
    public void testBeginTextBlockWithAttachedListener() throws Throwable {
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockStrategy = mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy extractionStrategy = new LocationTextExtractionStrategy(mockStrategy);
        RenderFilter[] filters = new RenderFilter[3];

        listener.attachRenderListener(extractionStrategy, filters);
        listener.beginTextBlock();
    }

    private RenderFilter[] createRegionTextRenderFilters() {
        Rectangle2D.Double rectangle = new Rectangle2D.Double(-2030.332760438, -2030.332760438, -2030.332760438, -846.7357066836466);
        RegionTextRenderFilter regionFilter = new RegionTextRenderFilter(rectangle);
        return new RenderFilter[]{regionFilter, regionFilter, regionFilter};
    }
}