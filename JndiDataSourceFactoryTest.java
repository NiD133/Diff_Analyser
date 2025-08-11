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
 * Test suite for JndiDataSourceFactory.
 */
class JndiDataSourceFactoryTest extends BaseDataTest {

  // Constants for test configuration
  private static final String MOCK_CONTEXT_FACTORY_CLASS_NAME = MockContextFactory.class.getName();
  private static final String MOCK_INITIAL_CONTEXT_PATH = "/mypath/path/";
  private static final String MOCK_DATA_SOURCE_NAME = "myDataSource";

  // Expected data source for comparison in tests
  private UnpooledDataSource expectedDataSource;

  @BeforeEach
  void setup() throws Exception {
    // Initialize the expected data source using predefined properties
    expectedDataSource = createUnpooledDataSource(BLOG_PROPERTIES);
  }

  @Test
  void shouldRetrieveDataSourceFromJNDI() {
    // Set up the mock JNDI environment
    setupMockJndiEnvironment();

    // Create and configure the JNDI data source factory
    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    factory.setProperties(createJndiProperties());

    // Retrieve the data source from the factory
    DataSource actualDataSource = factory.getDataSource();

    // Assert that the retrieved data source matches the expected data source
    assertEquals(expectedDataSource, actualDataSource);
  }

  /**
   * Sets up a mock JNDI environment with a data source binding.
   */
  private void setupMockJndiEnvironment() {
    try {
      // Create environment properties for the initial context
      Properties env = new Properties();
      env.put(Context.INITIAL_CONTEXT_FACTORY, MOCK_CONTEXT_FACTORY_CLASS_NAME);

      // Create a mock context and bind the expected data source
      MockContext mockContext = new MockContext(false);
      mockContext.bind(MOCK_DATA_SOURCE_NAME, expectedDataSource);

      // Bind the mock context to the initial context
      InitialContext initialContext = new InitialContext(env);
      initialContext.bind(MOCK_INITIAL_CONTEXT_PATH, mockContext);
    } catch (NamingException e) {
      throw new DataSourceException("Error configuring JndiDataSourceTransactionPool. Cause: " + e, e);
    }
  }

  /**
   * Creates properties for configuring the JNDI data source factory.
   *
   * @return Properties object with JNDI configuration
   */
  private Properties createJndiProperties() {
    Properties properties = new Properties();
    properties.setProperty(JndiDataSourceFactory.ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, MOCK_CONTEXT_FACTORY_CLASS_NAME);
    properties.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, MOCK_INITIAL_CONTEXT_PATH);
    properties.setProperty(JndiDataSourceFactory.DATA_SOURCE, MOCK_DATA_SOURCE_NAME);
    return properties;
  }

  /**
   * Mock implementation of InitialContextFactory for testing purposes.
   */
  public static class MockContextFactory implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
      return new MockContext(false);
    }
  }

  /**
   * Mock implementation of InitialContext for testing purposes.
   */
  public static class MockContext extends InitialContext {
    private static final Map<String, Object> bindings = new HashMap<>();

    MockContext(boolean lazy) throws NamingException {
      super(lazy);
    }

    @Override
    public Object lookup(String name) {
      return bindings.get(name);
    }

    @Override
    public void bind(String name, Object obj) {
      bindings.put(name, obj);
    }
  }
}