package org.apache.ibatis.datasource.jndi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that JndiDataSourceFactory can look up a DataSource from a JNDI Context.
 * This test uses a simple in-memory mock JNDI implementation.
 */
class JndiDataSourceFactoryTest extends BaseDataTest {

  private static final String INITIAL_CONTEXT_FACTORY_CLASS = MockContextFactory.class.getName();
  private static final String CONTEXT_BINDING_PATH = "/mypath/path/";
  private static final String DATASOURCE_JNDI_NAME = "myDataSource";

  private UnpooledDataSource expectedDataSource;

  @BeforeEach
  void setUp() throws Exception {
    expectedDataSource = createUnpooledDataSource(BLOG_PROPERTIES);
    MockContext.resetBindings();
  }

  @AfterEach
  void tearDown() {
    MockContext.resetBindings();
  }

  @Test
  void retrievesDataSourceFromJndi() {
    // Arrange: register a mock JNDI context and bind the expected DataSource
    bindDataSourceInMockJndi(CONTEXT_BINDING_PATH, DATASOURCE_JNDI_NAME, expectedDataSource);

    // Arrange: configure the factory with JNDI environment and names
    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    factory.setProperties(jndiProperties(INITIAL_CONTEXT_FACTORY_CLASS, CONTEXT_BINDING_PATH, DATASOURCE_JNDI_NAME));

    // Act
    DataSource actualDataSource = factory.getDataSource();

    // Assert
    assertEquals(expectedDataSource, actualDataSource);
  }

  private static Properties jndiProperties(String initialContextFactory, String initialContextPath, String dataSourceName) {
    Properties properties = new Properties();
    properties.setProperty(JndiDataSourceFactory.ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
    properties.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, initialContextPath);
    properties.setProperty(JndiDataSourceFactory.DATA_SOURCE, dataSourceName);
    return properties;
  }

  private static void bindDataSourceInMockJndi(String contextPath, String dataSourceName, DataSource dataSource) {
    try {
      // Tell InitialContext to use our mock factory
      Properties env = new Properties();
      env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY_CLASS);

      // Create a child context and bind the DataSource under the given name
      MockContext childCtx = new MockContext(false);
      childCtx.bind(dataSourceName, dataSource);

      // Bind the child context under the given path in the root context
      InitialContext rootCtx = new InitialContext(env);
      rootCtx.bind(contextPath, childCtx);
    } catch (NamingException e) {
      throw new DataSourceException("Error setting up mock JNDI for test.", e);
    }
  }

  /**
   * InitialContextFactory that returns our in-memory MockContext.
   */
  public static class MockContextFactory implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
      return new MockContext(false);
    }
  }

  /**
   * Minimal in-memory InitialContext implementation backed by a static map.
   * This is sufficient for lookup/bind operations used in the test.
   */
  public static class MockContext extends InitialContext {
    private static final Map<String, Object> BINDINGS = new ConcurrentHashMap<>();

    MockContext(boolean lazy) throws NamingException {
      super(lazy);
    }

    @Override
    public Object lookup(String name) {
      return BINDINGS.get(name);
    }

    @Override
    public void bind(String name, Object obj) {
      BINDINGS.put(name, obj);
    }

    static void resetBindings() {
      BINDINGS.clear();
    }
  }
}