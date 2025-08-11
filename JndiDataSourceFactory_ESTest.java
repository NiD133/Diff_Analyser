package org.apache.ibatis.datasource.jndi;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for JndiDataSourceFactory.
 * This class contains tests to verify the behavior of JndiDataSourceFactory.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class JndiDataSourceFactory_ESTest extends JndiDataSourceFactory_ESTest_scaffolding {

    /**
     * Test that setting null properties throws NullPointerException.
     */
    @Test(timeout = 4000)
    public void testSetNullPropertiesThrowsException() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        try {
            factory.setProperties(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    /**
     * Test that setting properties with non-string keys throws ClassCastException.
     */
    @Test(timeout = 4000)
    public void testSetPropertiesWithNonStringKeysThrowsException() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource("initial_context", "data_source", properties);
        Object value = new Object();
        properties.put(unpooledDataSource, value);
        
        try {
            factory.setProperties(properties);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    /**
     * Test setting valid properties.
     */
    @Test(timeout = 4000)
    public void testSetValidProperties() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("env.key1", "value1");
        properties.setProperty("env.key2", "value2");
        factory.setProperties(properties);
    }

    /**
     * Test that setting properties without required context throws RuntimeException.
     */
    @Test(timeout = 4000)
    public void testSetPropertiesWithoutContextThrowsException() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("data_source", "some_value");
        
        try {
            factory.setProperties(properties);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    /**
     * Test that setting properties with incorrect context throws RuntimeException.
     */
    @Test(timeout = 4000)
    public void testSetPropertiesWithIncorrectContextThrowsException() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("initial_context", "wrong_context");
        properties.setProperty("data_source", "wrong_context");
        
        try {
            factory.setProperties(properties);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }

    /**
     * Test setting properties from a StringReader.
     */
    @Test(timeout = 4000)
    public void testSetPropertiesFromStringReader() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        StringReader reader = new StringReader("initial_context");
        Properties properties = new Properties();
        properties.load(reader);
        factory.setProperties(properties);
    }

    /**
     * Test that getDataSource returns null when not configured.
     */
    @Test(timeout = 4000)
    public void testGetDataSourceReturnsNullWhenNotConfigured() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        DataSource dataSource = factory.getDataSource();
        assertNull(dataSource);
    }
}