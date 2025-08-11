package org.apache.ibatis.jdbc;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
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
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ScriptRunner_ESTest extends ScriptRunner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRunScriptWithNullReaderThrowsException() throws Throwable {
        ScriptRunner scriptRunner = new ScriptRunner(null);
        Reader nullReader = Reader.nullReader();

        try {
            scriptRunner.runScript(nullReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.jdbc.ScriptRunner", e);
        }
    }

    @Test(timeout = 4000)
    public void testRunScriptWithInvalidSQLThrowsException() throws Throwable {
        Connection connection = mock(Connection.class, new ViolatedAssumptionAnswer());
        doReturn(true, true, true).when(connection).getAutoCommit();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        StringReader invalidSql = new StringReader("Invalid SQL");

        try {
            scriptRunner.runScript(invalidSql);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.jdbc.ScriptRunner", e);
        }
    }

    @Test(timeout = 4000)
    public void testRunScriptWithValidSQL() throws Throwable {
        Statement statement = mock(Statement.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(statement).execute(anyString());
        doReturn(false, false).when(statement).getMoreResults();
        doReturn(0, 0, 0, 0, 0).when(statement).getUpdateCount();
        Connection connection = mock(Connection.class, new ViolatedAssumptionAnswer());
        doReturn(statement).when(connection).createStatement();
        doReturn(false, false).when(connection).getAutoCommit();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        StringReader validSql = new StringReader("SELECT 1;");

        scriptRunner.runScript(validSql);
    }

    @Test(timeout = 4000)
    public void testCloseConnectionDoesNotThrow() throws Throwable {
        Connection connection = mock(Connection.class, new ViolatedAssumptionAnswer());
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.closeConnection();
    }

    @Test(timeout = 4000)
    public void testSetEscapeProcessing() throws Throwable {
        ScriptRunner scriptRunner = new ScriptRunner(null);
        scriptRunner.setEscapeProcessing(true);
    }

    // Additional tests can be added here following the same pattern
}