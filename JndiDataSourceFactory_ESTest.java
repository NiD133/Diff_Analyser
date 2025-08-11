/*
 * Test suite for JndiDataSourceFactory
 * Tests the JNDI-based DataSource factory functionality
 */

package org.apache.ibatis.datasource.jndi;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringReader;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class JndiDataSourceFactory_ESTest extends JndiDataSourceFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSetProperties_WithNullProperties_ShouldThrowNullPointerException() throws Throwable {
        // Given
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        
        // When & Then
        try { 
            factory.setProperties(null);
            fail("Expected NullPointerException when properties is null");
        } catch(NullPointerException e) {
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetProperties_WithInvalidPropertyKeys_ShouldThrowClassCastException() throws Throwable {
        // Given
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        
        // Create invalid property with non-String key
        UnpooledDataSource invalidKey = new UnpooledDataSource("initial_context", "data_source", properties);
        Object value = new Object();
        properties.put(invalidKey, value);
        
        // When & Then
        try { 
            factory.setProperties(properties);
            fail("Expected ClassCastException when property key is not a String");
        } catch(ClassCastException e) {
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetProperties_WithEmptyProperties_ShouldSucceed() throws Throwable {
        // Given
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties emptyProperties = new Properties();
        
        // Load empty content from StringReader
        StringReader emptyReader = new StringReader("env.");
        emptyProperties.load(emptyReader);
        
        StringReader anotherEmptyReader = new StringReader("env.env$ource");
        emptyProperties.load(anotherEmptyReader);
        
        // When & Then (should not throw exception)
        factory.setProperties(emptyProperties);
    }

    @Test(timeout = 4000)
    public void testSetProperties_WithDataSourceProperty_ShouldThrowRuntimeExceptionDueToMissingJndiContext() throws Throwable {
        // Given
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        
        // Load properties that will trigger JNDI lookup
        StringReader reader = new StringReader("data_source");
        properties.load(reader);
        
        // When & Then
        try { 
            factory.setProperties(properties);
            fail("Expected RuntimeException due to missing JNDI initial context");
        } catch(RuntimeException e) {
            assertTrue("Should contain NoInitialContextException message", 
                      e.getMessage().contains("javax.naming.NoInitialContextException"));
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetProperties_WithInitialContextAndDataSourceProperties_ShouldThrowRuntimeExceptionDueToMissingJndiContext() throws Throwable {
        // Given
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        
        // Set both required JNDI properties
        properties.setProperty("initial_context", "initial_context");
        properties.setProperty("data_source", "initial_context");
        
        // When & Then
        try { 
            factory.setProperties(properties);
            fail("Expected RuntimeException due to missing JNDI factory class");
        } catch(RuntimeException e) {
            assertTrue("Should contain NoInitialContextException message", 
                      e.getMessage().contains("javax.naming.NoInitialContextException"));
            assertTrue("Should mention missing factory.initial property", 
                      e.getMessage().contains("java.naming.factory.initial"));
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetProperties_WithOnlyInitialContextProperty_ShouldSucceed() throws Throwable {
        // Given
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        
        // Load properties with only initial_context (no data_source lookup needed)
        StringReader reader = new StringReader("initial_context");
        properties.load(reader);
        
        // When & Then (should not throw exception)
        factory.setProperties(properties);
    }

    @Test(timeout = 4000)
    public void testGetDataSource_WithoutConfiguration_ShouldReturnNull() throws Throwable {
        // Given
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        
        // When
        DataSource dataSource = factory.getDataSource();
        
        // Then
        assertNull("DataSource should be null when not configured", dataSource);
    }
}