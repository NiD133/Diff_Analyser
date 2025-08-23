package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.DeprecatedAttributes;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the DeprecatedAttributes class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DeprecatedAttributes_ESTest extends DeprecatedAttributes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testBuilderSetsForRemoval() throws Throwable {
        // Arrange
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        
        // Act
        builder.setForRemoval(true);
        DeprecatedAttributes attributes = builder.get();
        
        // Assert
        assertTrue(attributes.isForRemoval());
    }

    @Test(timeout = 4000)
    public void testBuilderSetsSince() throws Throwable {
        // Arrange
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        
        // Act
        builder.setSince("+,ygu");
        DeprecatedAttributes attributes = builder.get();
        
        // Assert
        assertEquals("+,ygu", attributes.getSince());
        assertEquals("", attributes.getDescription());
        assertFalse(attributes.isForRemoval());
    }

    @Test(timeout = 4000)
    public void testBuilderSetsDescription() throws Throwable {
        // Arrange
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        
        // Act
        builder.setDescription("RSb&;Wj}.&C.b S?5");
        DeprecatedAttributes attributes = builder.get();
        
        // Assert
        assertEquals("RSb&;Wj}.&C.b S?5", attributes.getDescription());
        assertEquals("", attributes.getSince());
        assertFalse(attributes.isForRemoval());
    }

    @Test(timeout = 4000)
    public void testToStringWithSince() throws Throwable {
        // Arrange
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        
        // Act
        builder.setSince("x-qz[]");
        DeprecatedAttributes attributes = builder.get();
        
        // Assert
        assertEquals("Deprecated since x-qz[]", attributes.toString());
    }

    @Test(timeout = 4000)
    public void testToStringForRemoval() throws Throwable {
        // Arrange
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        
        // Act
        builder.setForRemoval(true);
        DeprecatedAttributes attributes = builder.get();
        
        // Assert
        assertEquals("Deprecated for removal", attributes.toString());
    }

    @Test(timeout = 4000)
    public void testDefaultAttributesDescription() throws Throwable {
        // Arrange
        DeprecatedAttributes attributes = DeprecatedAttributes.DEFAULT;
        
        // Assert
        assertEquals("", attributes.getDescription());
    }

    @Test(timeout = 4000)
    public void testDefaultAttributesIsForRemoval() throws Throwable {
        // Arrange
        DeprecatedAttributes attributes = DeprecatedAttributes.DEFAULT;
        
        // Assert
        assertFalse(attributes.isForRemoval());
    }

    @Test(timeout = 4000)
    public void testDefaultAttributesSince() throws Throwable {
        // Arrange
        DeprecatedAttributes attributes = DeprecatedAttributes.DEFAULT;
        
        // Assert
        assertEquals("", attributes.getSince());
    }

    @Test(timeout = 4000)
    public void testToStringWithDescription() throws Throwable {
        // Arrange
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        
        // Act
        builder.setDescription(",");
        DeprecatedAttributes attributes = builder.get();
        
        // Assert
        assertEquals("Deprecated: ,", attributes.toString());
        assertEquals(",", attributes.getDescription());
    }
}