package org.apache.ibatis.datasource.jndi;

import org.apache.ibatis.datasource.DataSourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NoInitialContextException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link JndiDataSourceFactory}.
 */
class JndiDataSourceFactoryTest {

    private JndiDataSourceFactory jndiDataSourceFactory;

    @BeforeEach
    void setUp() {
        this.jndiDataSourceFactory = new JndiDataSourceFactory();
    }

    @Test
    void setPropertiesShouldThrowExceptionWhenInitialContextIsMissing() {
        // Arrange: Configure properties with a JNDI data source name but without
        // the required initial context information.
        Properties properties = new Properties();
        properties.setProperty(JndiDataSourceFactory.DATA_SOURCE, "java:comp/env/jdbc/testDB");

        // Act & Assert: Expect a DataSourceException because the JNDI environment
        // (initial context) has not been configured.
        DataSourceException thrown = assertThrows(
            DataSourceException.class,
            () -> jndiDataSourceFactory.setProperties(properties),
            "Should throw DataSourceException when initial context is not set."
        );

        // Verify that the underlying cause is the expected JNDI exception.
        Throwable cause = thrown.getCause();
        assertNotNull(cause, "Exception should have a cause.");
        assertInstanceOf(NoInitialContextException.class, cause, "The cause should be a NoInitialContextException.");
    }
}