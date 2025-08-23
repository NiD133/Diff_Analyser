package org.apache.ibatis.datasource.jndi;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JndiDataSourceFactory_ESTestTest3 extends JndiDataSourceFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        JndiDataSourceFactory jndiDataSourceFactory0 = new JndiDataSourceFactory();
        Properties properties0 = new Properties();
        StringReader stringReader0 = new StringReader("env.");
        properties0.load((Reader) stringReader0);
        StringReader stringReader1 = new StringReader("env.env$ource");
        properties0.load((Reader) stringReader1);
        jndiDataSourceFactory0.setProperties(properties0);
    }
}
