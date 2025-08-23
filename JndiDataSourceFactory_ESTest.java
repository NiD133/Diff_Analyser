package org.apache.ibatis.datasource.jndi;

import org.apache.ibatis.datasource.DataSourceException;
import org.junit.Test;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Unit tests for JndiDataSourceFactory that focus on readability and intent.
 *
 * These tests verify:
 * - default state (no DataSource configured)
 * - null/invalid input handling
 * - environment-only properties handling
 * - behavior when JNDI is not available on the JVM (common on unit test VMs)
 */
public class JndiDataSourceFactoryTest {

  @Test
  public void getDataSource_defaultsToNull() {
    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    DataSource ds = factory.getDataSource();

    assertNull("Factory should not have a DataSource before configuration", ds);
  }

  @Test
  public void setProperties_withNull_throwsNullPointerException() {
    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    assertThrows(NullPointerException.class, () -> factory.setProperties(null));
  }

  @Test
  public void setProperties_withNonStringKey_throwsClassCastException() {
    // Properties technically allows non-String keys, but the factory expects String keys.
    Properties props = new Properties();
    props.put(new Object(), "value"); // Non-String key

    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    assertThrows(ClassCastException.class, () -> factory.setProperties(props));
  }

  @Test
  public void setProperties_withOnlyEnvProps_doesNotResolveDataSource() {
    // Only env.* entries should not trigger a JNDI lookup by themselves.
    Properties props = new Properties();
    props.setProperty(JndiDataSourceFactory.ENV_PREFIX + "java.naming.factory.initial", "com.example.MockInitialContextFactory");

    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    factory.setProperties(props);

    assertNull("Without a data source name, no DataSource should be resolved", factory.getDataSource());
  }

  @Test
  public void setProperties_withOnlyDataSourceName_throwsDataSourceException_whenNoJndi() {
    // Typical unit test JVMs do not have a default JNDI InitialContext set up,
    // so lookup should fail with a DataSourceException wrapping a NamingException.
    Properties props = new Properties();
    props.setProperty(JndiDataSourceFactory.DATA_SOURCE, "jdbc/MyDataSource");

    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    DataSourceException ex = assertThrows(DataSourceException.class, () -> factory.setProperties(props));
    assertTrue("Expected cause to be a NamingException", ex.getCause() instanceof NamingException);
  }

  @Test
  public void setProperties_withInitialContextAndDataSource_throwsDataSourceException_whenNoJndi() {
    // When both initial_context and data_source are set but no JNDI is available,
    // the factory should raise a DataSourceException with a NamingException cause.
    Properties props = new Properties();
    props.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, "java:comp/env");
    props.setProperty(JndiDataSourceFactory.DATA_SOURCE, "jdbc/MyDataSource");

    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    DataSourceException ex = assertThrows(DataSourceException.class, () -> factory.setProperties(props));
    assertTrue("Expected cause to be a NamingException", ex.getCause() instanceof NamingException);
  }

  @Test
  public void setProperties_withOnlyInitialContext_doesNotLookupOrFail() {
    // Only providing initial_context should not trigger a lookup on its own.
    Properties props = new Properties();
    props.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, "java:comp/env");

    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    factory.setProperties(props);

    assertNull("Without a data source name, no DataSource should be resolved", factory.getDataSource());
  }
}