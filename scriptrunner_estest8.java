package org.apache.ibatis.jdbc;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Statement;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class ScriptRunner_ESTestTest8 extends ScriptRunner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        ResultSetMetaData resultSetMetaData0 = mock(ResultSetMetaData.class, new ViolatedAssumptionAnswer());
        doReturn(1649).when(resultSetMetaData0).getColumnCount();
        doReturn((String) null, (String) null, (String) null, (String) null, (String) null).when(resultSetMetaData0).getColumnLabel(anyInt());
        ResultSet resultSet0 = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(resultSetMetaData0).when(resultSet0).getMetaData();
        doReturn((String) null, (String) null, (String) null, (String) null, (String) null).when(resultSet0).getString(anyInt());
        doReturn(true, false).when(resultSet0).next();
        Statement statement0 = mock(Statement.class, new ViolatedAssumptionAnswer());
        doReturn(true).when(statement0).execute(anyString());
        doReturn(false, false, false, false, false).when(statement0).getMoreResults();
        doReturn(resultSet0).when(statement0).getResultSet();
        doReturn(0, 0, 0, 0, 0).when(statement0).getUpdateCount();
        Connection connection0 = mock(Connection.class, new ViolatedAssumptionAnswer());
        doReturn(statement0).when(connection0).createStatement();
        doReturn(true, true).when(connection0).getAutoCommit();
        ScriptRunner scriptRunner0 = new ScriptRunner(connection0);
        StringReader stringReader0 = new StringReader(";");
        // Undeclared exception!
        try {
            scriptRunner0.runScript(stringReader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error executing:
            // .  Cause: org.evosuite.runtime.TooManyResourcesException: Loop has been executed more times than the allowed 10000
            //
            verifyException("org.apache.ibatis.jdbc.ScriptRunner", e);
        }
    }
}
