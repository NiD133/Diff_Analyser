package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
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

/**
 * Test suite for MultiFilteredRenderListener class.
 * Tests various scenarios including normal operations and error conditions.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class MultiFilteredRenderListener_ESTest extends MultiFilteredRenderListener_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEndTextBlockWithValidListener() throws Throwable {
        // Given: A MultiFilteredRenderListener with an attached text extraction strategy
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationStrategy = 
            mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy textExtractionStrategy = new LocationTextExtractionStrategy(mockLocationStrategy);
        RenderFilter[] emptyFilters = new RenderFilter[7];
        
        listener.attachRenderListener(textExtractionStrategy, emptyFilters);
        
        // When: endTextBlock is called
        // Then: No exception should be thrown
        listener.endTextBlock();
    }

    @Test(timeout = 4000)
    public void testBeginTextBlockWithNullListener() throws Throwable {
        // Given: A MultiFilteredRenderListener with a null listener attached
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        RenderFilter[] emptyFilters = new RenderFilter[0];
        listener.attachRenderListener(null, emptyFilters);
        
        // When: beginTextBlock is called
        // Then: NullPointerException should be thrown
        try { 
            listener.beginTextBlock();
            fail("Expected NullPointerException when calling beginTextBlock with null listener");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderImageWithRegionFilter() throws Throwable {
        // Given: A MultiFilteredRenderListener with region-based text render filters
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationStrategy = 
            mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy textExtractionStrategy = new LocationTextExtractionStrategy(mockLocationStrategy);
        
        // Create region filter with specific coordinates
        Rectangle2D.Double region = new Rectangle2D.Double(-2030.332760438, -2030.332760438, 
                                                           -2030.332760438, -846.7357066836466);
        RegionTextRenderFilter regionFilter = new RegionTextRenderFilter(region);
        
        RenderFilter[] filters = new RenderFilter[3];
        filters[0] = regionFilter;
        filters[1] = regionFilter;
        filters[2] = regionFilter;
        
        listener.attachRenderListener(textExtractionStrategy, filters);
        
        // When: renderImage is called with null ImageRenderInfo
        // Then: No exception should be thrown (filters handle null gracefully)
        listener.renderImage(null);
    }

    @Test(timeout = 4000)
    public void testRenderImageWithNullFilters() throws Throwable {
        // Given: A MultiFilteredRenderListener with null filters
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationStrategy = 
            mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy textExtractionStrategy = new LocationTextExtractionStrategy(mockLocationStrategy);
        RenderFilter[] filtersWithNulls = new RenderFilter[3]; // Array contains nulls
        
        listener.attachRenderListener(textExtractionStrategy, filtersWithNulls);
        
        // When: renderImage is called with null ImageRenderInfo
        // Then: NullPointerException should be thrown due to null filters
        try { 
            listener.renderImage(null);
            fail("Expected NullPointerException when using null filters");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testEndTextBlockWithNullListenerAndFilters() throws Throwable {
        // Given: A MultiFilteredRenderListener with both null listener and null filters
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        listener.attachRenderListener(null, null);
        
        // When: endTextBlock is called
        // Then: NullPointerException should be thrown
        try { 
            listener.endTextBlock();
            fail("Expected NullPointerException when both listener and filters are null");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithValidListenerButNullTextInfo() throws Throwable {
        // Given: A MultiFilteredRenderListener with valid listener but no filters
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy textExtractionStrategy = new LocationTextExtractionStrategy();
        RenderFilter[] emptyFilters = new RenderFilter[0];
        
        listener.attachRenderListener(textExtractionStrategy, emptyFilters);
        
        // When: renderText is called with null TextRenderInfo
        // Then: NullPointerException should be thrown by the text extraction strategy
        try { 
            listener.renderText(null);
            fail("Expected NullPointerException when TextRenderInfo is null");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNullListener() throws Throwable {
        // Given: A MultiFilteredRenderListener with null listener
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        RenderFilter[] singleFilter = new RenderFilter[1];
        listener.attachRenderListener(null, singleFilter);
        
        // When: renderText is called with null TextRenderInfo
        // Then: NullPointerException should be thrown
        try { 
            listener.renderText(null);
            fail("Expected NullPointerException when listener is null");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.MultiFilteredRenderListener", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNoAttachedListeners() throws Throwable {
        // Given: A MultiFilteredRenderListener with no attached listeners
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        
        // When: renderText is called with null TextRenderInfo
        // Then: No exception should be thrown (no listeners to process)
        listener.renderText(null);
    }

    @Test(timeout = 4000)
    public void testBeginTextBlockWithNoAttachedListeners() throws Throwable {
        // Given: A MultiFilteredRenderListener with no attached listeners
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        
        // When: beginTextBlock is called
        // Then: No exception should be thrown (no listeners to process)
        listener.beginTextBlock();
    }

    @Test(timeout = 4000)
    public void testBeginTextBlockWithValidListener() throws Throwable {
        // Given: A MultiFilteredRenderListener with a valid attached listener
        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationStrategy = 
            mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class, new ViolatedAssumptionAnswer());
        LocationTextExtractionStrategy textExtractionStrategy = new LocationTextExtractionStrategy(mockLocationStrategy);
        RenderFilter[] filters = new RenderFilter[3];
        
        listener.attachRenderListener(textExtractionStrategy, filters);
        
        // When: beginTextBlock is called
        // Then: No exception should be thrown
        listener.beginTextBlock();
    }
}