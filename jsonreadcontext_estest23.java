package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

// EvoSuite-specific imports are retained to maintain the original test execution environment.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonReadContext_ESTestTest23 extends JsonReadContext_ESTest_scaffolding {

    /**
     * Tests that a root JsonReadContext created without a DupDetector
     * is initialized with the correct default state.
     */
    @Test
    public void createRootContextWithoutDupDetectorShouldHaveCorrectInitialState() {
        // Arrange & Act: Create a root context without a duplicate detector.
        // The method under test is `JsonReadContext.createRootContext(DupDetector)`.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);

        // Assert: Verify the properties of the newly created root context.
        assertTrue("A newly created context should be in the root state.", rootContext.inRoot());
        assertEquals("The nesting depth of a root context should be 0.", 0, rootContext.getNestingDepth());
        assertEquals("The initial entry count of a root context should be 0.", 0, rootContext.getEntryCount());
        assertNull("The duplicate detector should be null as it was not provided.", rootContext.getDupDetector());
    }
}