/*
 * Refactored test suite for ScriptRunner with improved readability and maintainability
 */
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
import org.apache.ibatis.jdbc.ScriptRunner;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ScriptRunner_ESTest extends ScriptRunner_ESTest_scaffolding {

    // ========================================================================
    // Tests for exception handling scenarios
    // ========================================================================

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void runScriptWithSendFullScriptEnabledAndNullReader_ThrowsRuntimeException() throws Throwable {
        // Setup: Mock connection with statement returning result sets
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        doReturn(-2539).when(metaData).getColumnCount();

        ResultSet resultSet = mock(ResultSet.class);
        doReturn(metaData).when(resultSet).getMetaData();
        doReturn(true, false).when(resultSet).next();

        Statement statement = mock(Statement.class);
        doReturn(true).when(statement).execute(anyString());
        doReturn(false).when(statement).getMoreResults();
        doReturn(resultSet).when(statement).getResultSet();
        doReturn(0).when(statement).getUpdateCount();

        Connection connection = mock(Connection.class);
        doReturn(statement).when(connection).createStatement();
        doReturn(false).when(connection).getAutoCommit();

        // Configure script runner
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setSendFullScript(true);

        // Execute test
        runner.runScript(Reader.nullReader());
    }

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void runScriptWithIncompleteLine_ThrowsMissingTerminatorException() throws Throwable {
        // Setup: Mock connection with auto-commit enabled
        Connection connection = mock(Connection.class);
        doReturn(true).when(connection).getAutoCommit();

        // Configure and execute
        ScriptRunner runner = new ScriptRunner(connection);
        runner.runScript(new StringReader("M&.?G0tfcHspB "));
    }

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void runScriptWithNullConnection_ThrowsAutoCommitException() throws Throwable {
        // Setup: Null connection
        ScriptRunner runner = new ScriptRunner(null);
        
        // Execute test
        runner.runScript(Reader.nullReader());
    }

    // ========================================================================
    // Tests for warning handling scenarios
    // ========================================================================

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void runScriptWithThrowWarningEnabled_ThrowsOnSQLWarning() throws Throwable {
        // Setup: Create SQL warning
        SQLWarning warning = mock(SQLWarning.class);
        doReturn("5MBV").when(warning).toString();

        Statement statement = mock(Statement.class);
        doReturn(true).when(statement).execute(anyString());
        doReturn(warning).when(statement).getWarnings();

        Connection connection = mock(Connection.class);
        doReturn(statement).when(connection).createStatement();
        doReturn(true).when(connection).getAutoCommit();

        // Configure script runner
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setThrowWarning(true);
        runner.setDelimiter("5MBV");

        // Execute test
        runner.runScript(new StringReader("5MBV"));
    }

    // ========================================================================
    // Tests for comment and whitespace handling
    // ========================================================================

    @Test(timeout = 4000)
    public void runScriptWithSingleLineComment_ProcessesSuccessfully() throws Throwable {
        Connection connection = mock(Connection.class);
        doReturn(false).when(connection).getAutoCommit();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.runScript(new StringReader("--U1!-k'1|Ud7jJjpCMf"));
    }

    @Test(timeout = 4000)
    public void runScriptWithDoubleSlashComment_ProcessesSuccessfully() throws Throwable {
        Connection connection = mock(Connection.class);
        doReturn(false).when(connection).getAutoCommit();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.runScript(new StringReader("//--rg.apache.ibatis.jdbc.ScriptRunner"));
    }

    @Test(timeout = 4000)
    public void runScriptWithNewlineOnly_ProcessesSuccessfully() throws Throwable {
        Connection connection = mock(Connection.class);
        doReturn(false).when(connection).getAutoCommit();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.runScript(new StringReader("\n"));
    }

    // ========================================================================
    // Tests for connection handling
    // ========================================================================

    @Test(timeout = 4000)
    public void closeConnectionWithValidConnection_ExecutesSuccessfully() throws Throwable {
        Connection connection = mock(Connection.class);
        ScriptRunner runner = new ScriptRunner(connection);
        runner.closeConnection();
    }

    @Test(timeout = 4000)
    public void closeConnectionWithNullConnection_ExecutesSuccessfully() throws Throwable {
        ScriptRunner runner = new ScriptRunner(null);
        runner.closeConnection();
    }

    // ========================================================================
    // Tests for configuration methods
    // ========================================================================

    @Test(timeout = 4000)
    public void setEscapeProcessingWithNullConnection_ExecutesSuccessfully() throws Throwable {
        ScriptRunner runner = new ScriptRunner(null);
        runner.setEscapeProcessing(true);
    }

    // ========================================================================
    // Tests for successful execution scenarios
    // ========================================================================

    @Test(timeout = 4000)
    public void runScriptWithValidCommand_ExecutesSuccessfully() throws Throwable {
        // Setup: Mock successful statement execution
        Statement statement = mock(Statement.class);
        doReturn(false).when(statement).execute(anyString());
        doReturn(false).when(statement).getMoreResults();
        doReturn(705, -227290201, -1).when(statement).getUpdateCount();

        Connection connection = mock(Connection.class);
        doReturn(statement).when(connection).createStatement();
        doReturn(true).when(connection).getAutoCommit();

        // Execute test
        ScriptRunner runner = new ScriptRunner(connection);
        runner.runScript(new StringReader("_DO3)u?Yu1;[5^Fn"));
    }
}