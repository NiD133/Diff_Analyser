package org.apache.ibatis.datasource.jndi;

import static org.junit.Assert.assertNull;

import javax.sql.DataSource;
import org.junit.Test;

/**
 * Test suite for {@link JndiDataSourceFactory}.
 */
public class JndiDataSourceFactoryTest {

    @Test
    public void shouldReturnNullDataSourceWhenNotConfigured() {
        // Arrange: Create a new factory without setting any properties.
        JndiDataSourceFactory factory = new JndiDataSourceFactory();

        // Act: Attempt to get the data source.
        DataSource dataSource = factory.getDataSource();

        // Assert: The data source should be null as it has not been initialized via JNDI lookup.
        assertNull("DataSource should be null before properties are set", dataSource);
    }
}