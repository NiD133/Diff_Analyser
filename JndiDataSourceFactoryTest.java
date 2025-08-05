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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JndiDataSourceFactoryTest extends BaseDataTest {

    private static final String TEST_INITIAL_CONTEXT_FACTORY = MockContextFactory.class.getName();
    private static final String TEST_INITIAL_CONTEXT = "/mypath/path/";
    private static final String TEST_DATA_SOURCE = "myDataSource";
    
    private UnpooledDataSource expectedDataSource;

    @BeforeEach
    void setup() throws Exception {
        expectedDataSource = createUnpooledDataSource(BLOG_PROPERTIES);
    }

    @AfterEach
    void tearDown() {
        // Clear static bindings after each test to ensure isolation
        MockContext.clearBindings();
    }

    @Test
    void shouldRetrieveDataSourceFromJNDI() {
        // Arrange: Set up JNDI environment with test data source
        setupJndiEnvironment();
        
        // Configure factory properties
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        factory.setProperties(createFactoryProperties());
        
        // Act: Retrieve data source via factory
        DataSource actualDataSource = factory.getDataSource();
        
        // Assert: Verify correct data source is retrieved
        assertEquals(expectedDataSource, actualDataSource);
    }

    /**
     * Configures JNDI environment with mock context and binds test data source.
     */
    private void setupJndiEnvironment() {
        try {
            Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, TEST_INITIAL_CONTEXT_FACTORY);

            // Create mock context and bind data source
            MockContext ctx = new MockContext(false);
            ctx.bind(TEST_DATA_SOURCE, expectedDataSource);

            // Bind mock context to JNDI
            InitialContext initCtx = new InitialContext(env);
            initCtx.bind(TEST_INITIAL_CONTEXT, ctx);
        } catch (NamingException e) {
            throw new DataSourceException("Error configuring JNDI environment: " + e, e);
        }
    }

    /**
     * Creates configuration properties for the JNDI data source factory.
     */
    private Properties createFactoryProperties() {
        Properties props = new Properties();
        // Set JNDI environment properties
        props.setProperty(JndiDataSourceFactory.ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, TEST_INITIAL_CONTEXT_FACTORY);
        props.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, TEST_INITIAL_CONTEXT);
        props.setProperty(JndiDataSourceFactory.DATA_SOURCE, TEST_DATA_SOURCE);
        return props;
    }

    /**
     * Mock InitialContextFactory implementation for testing JNDI lookups.
     */
    public static class MockContextFactory implements InitialContextFactory {
        @Override
        public Context getInitialContext(Hashtable<?, ?> environment) {
            try {
                return new MockContext(false);
            } catch (NamingException e) {
                throw new DataSourceException("Failed to create mock context", e);
            }
        }
    }

    /**
     * Mock JNDI Context implementation with simple in-memory bindings.
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

        /**
         * Clears all JNDI bindings for test isolation.
         */
        public static void clearBindings() {
            bindings.clear();
        }
    }
}