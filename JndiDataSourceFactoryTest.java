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
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Refactored test for JndiDataSourceFactory.
 *
 * Key improvements:
 * 1. Thread-safe test isolation using ThreadLocal for the mock JNDI context.
 * 2. Clear Arrange-Act-Assert structure within the test method.
 * 3. Simplified property setup and explicit test environment configuration.
 * 4. Removal of static state from mock objects to prevent test interference.
 */
class JndiDataSourceFactoryTest extends BaseDataTest {

  private static final String TEST_INITIAL_CONTEXT_FACTORY = MockContextFactory.class.getName();
  private static final String TEST_INITIAL_CONTEXT = "java:comp/env";
  private static final String TEST_DATA_SOURCE = "jdbc/testDataSource";

  // Use a ThreadLocal to ensure each test has an isolated JNDI context, making tests thread-safe.
  private static final ThreadLocal<Context> initialContextThreadLocal = new ThreadLocal<>();

  private UnpooledDataSource expectedDataSource;

  @BeforeEach
  void setupTest() throws Exception {
    // Prepare the DataSource object that we expect the factory to return.
    expectedDataSource = createUnpooledDataSource(BLOG_PROPERTIES);

    // Initialize the mock JNDI context for the current test thread.
    MockContext context = new MockContext(false);
    initialContextThreadLocal.set(context);
  }

  @AfterEach
  void tearDownTest() {
    // Clean up the context after the test to prevent state leakage and memory leaks.
    initialContextThreadLocal.remove();
  }

  @Test
  void shouldReturnDataSourceWhenPropertiesAreSet() throws NamingException {
    // Arrange: Set up the test environment and inputs.

    // 1. Configure the mock JNDI environment with a subcontext and the data source.
    // This simulates a typical application server's JNDI structure.
    Context context = initialContextThreadLocal.get();
    Context subcontext = new MockContext(false);
    subcontext.bind(TEST_DATA_SOURCE, expectedDataSource);
    context.bind(TEST_INITIAL_CONTEXT, subcontext);

    // 2. Configure the JndiDataSourceFactory with the necessary properties to find the DataSource.
    Properties props = new Properties();
    props.setProperty(JndiDataSourceFactory.ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, TEST_INITIAL_CONTEXT_FACTORY);
    props.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, TEST_INITIAL_CONTEXT);
    props.setProperty(JndiDataSourceFactory.DATA_SOURCE, TEST_DATA_SOURCE);

    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    factory.setProperties(props);

    // Act: Execute the method under test.
    DataSource actualDataSource = factory.getDataSource();

    // Assert: Verify the outcome.
    assertEquals(expectedDataSource, actualDataSource, "The factory should return the exact DataSource instance bound in JNDI.");
  }

  /**
   * A mock InitialContextFactory that provides the test-specific, thread-local Context.
   */
  public static class MockContextFactory implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) {
      return initialContextThreadLocal.get();
    }
  }

  /**
   * A mock JNDI Context that uses an instance-level map for bindings, ensuring test isolation.
   */
  public static class MockContext extends InitialContext {
    // Bindings are stored per instance, not statically, to isolate tests.
    private final Map<String, Object> bindings = new HashMap<>();

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