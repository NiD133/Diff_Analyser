/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.datasource.jndi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for JndiDataSourceFactory to verify JNDI-based DataSource retrieval.
 * 
 * This test uses mock JNDI context to simulate a real JNDI environment
 * without requiring an actual application server.
 */
class JndiDataSourceFactoryTest extends BaseDataTest {

  // JNDI configuration constants
  private static final String MOCK_CONTEXT_FACTORY_CLASS = MockContextFactory.class.getName();
  private static final String JNDI_CONTEXT_PATH = "/mypath/path/";
  private static final String DATA_SOURCE_JNDI_NAME = "myDataSource";
  
  private UnpooledDataSource expectedDataSource;

  @BeforeEach
  void setupTestDataSource() throws Exception {
    // Create the DataSource that we expect to retrieve from JNDI
    expectedDataSource = createUnpooledDataSource(BLOG_PROPERTIES);
  }

  @Test
  void shouldRetrieveDataSourceFromJndiContext() {
    // Given: A DataSource is bound in the JNDI context
    bindDataSourceInJndiContext();
    
    // When: JndiDataSourceFactory is configured and asked for a DataSource
    JndiDataSourceFactory factory = createConfiguredJndiFactory();
    DataSource actualDataSource = factory.getDataSource();
    
    // Then: The factory should return the DataSource from JNDI
    assertEquals(expectedDataSource, actualDataSource, 
        "Factory should return the DataSource that was bound in JNDI");
  }

  /**
   * Creates and configures a JndiDataSourceFactory with the necessary JNDI properties.
   */
  private JndiDataSourceFactory createConfiguredJndiFactory() {
    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    Properties jndiProperties = createJndiProperties();
    factory.setProperties(jndiProperties);
    return factory;
  }

  /**
   * Creates the properties needed to configure JNDI lookup.
   */
  private Properties createJndiProperties() {
    Properties properties = new Properties();
    
    // Configure the mock JNDI context factory
    properties.setProperty(
        JndiDataSourceFactory.ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, 
        MOCK_CONTEXT_FACTORY_CLASS);
    
    // Set the JNDI context path where the DataSource is located
    properties.setProperty(
        JndiDataSourceFactory.INITIAL_CONTEXT, 
        JNDI_CONTEXT_PATH);
    
    // Set the JNDI name of the DataSource
    properties.setProperty(
        JndiDataSourceFactory.DATA_SOURCE, 
        DATA_SOURCE_JNDI_NAME);
    
    return properties;
  }

  /**
   * Sets up the mock JNDI environment and binds the test DataSource.
   * This simulates what an application server would do when deploying a DataSource.
   */
  private void bindDataSourceInJndiContext() {
    try {
      // Create JNDI environment properties
      Properties jndiEnvironment = new Properties();
      jndiEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, MOCK_CONTEXT_FACTORY_CLASS);

      // Create mock context and bind our test DataSource
      MockContext dataSourceContext = new MockContext(false);
      dataSourceContext.bind(DATA_SOURCE_JNDI_NAME, expectedDataSource);

      // Bind the context at the specified path
      InitialContext rootContext = new InitialContext(jndiEnvironment);
      rootContext.bind(JNDI_CONTEXT_PATH, dataSourceContext);
      
    } catch (NamingException e) {
      throw new DataSourceException(
          "Failed to set up mock JNDI environment for testing. Cause: " + e, e);
    }
  }

  /**
   * Mock implementation of InitialContextFactory for testing.
   * This allows us to test JNDI functionality without a real JNDI provider.
   */
  public static class MockContextFactory implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
      return new MockContext(false);
    }
  }

  /**
   * Mock JNDI Context implementation that stores bindings in memory.
   * This simulates a real JNDI context for testing purposes.
   */
  public static class MockContext extends InitialContext {
    // Shared storage for all JNDI bindings in tests
    private static final Map<String, Object> jndiBindings = new HashMap<>();

    MockContext(boolean lazy) throws NamingException {
      super(lazy);
    }

    @Override
    public Object lookup(String jndiName) {
      return jndiBindings.get(jndiName);
    }

    @Override
    public void bind(String jndiName, Object object) {
      jndiBindings.put(jndiName, object);
    }
  }
}