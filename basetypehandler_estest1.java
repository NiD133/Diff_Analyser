package org.apache.ibatis.type;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Test suite for {@link BaseTypeHandler}.
 *
 * This test focuses on the generic parameter setting logic in the base class.
 * We use a concrete implementation, {@link StringTypeHandler}, to test the abstract
 * parent's behavior, specifically how it handles null and non-null parameters.
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseTypeHandlerTest {

    @Mock
    private PreparedStatement preparedStatementMock;

    private StringTypeHandler stringTypeHandler;

    @Before
    public void setUp() {
        stringTypeHandler = new StringTypeHandler();
    }

    @Test
    public void shouldDelegateToSetNonNullParameterWhenParameterIsNotNull() throws SQLException {
        // Arrange
        final int parameterIndex = 1;
        final String testValue = "Test";
        // Use a JdbcType that is appropriate for the data being set.
        final JdbcType jdbcType = JdbcType.VARCHAR;

        // Act
        stringTypeHandler.setParameter(preparedStatementMock, parameterIndex, testValue, jdbcType);

        // Assert
        // The primary responsibility of BaseTypeHandler.setParameter is to delegate to
        // setNonNullParameter for non-null values. For StringTypeHandler, this
        // results in a call to PreparedStatement.setString().
        verify(preparedStatementMock).setString(parameterIndex, testValue);

        // Verify that no other interactions with the mock occurred.
        verifyNoMoreInteractions(preparedStatementMock);
    }

    @Test
    public void shouldCallSetNullOnPreparedStatementWhenParameterIsNull() throws SQLException {
        // Arrange
        final int parameterIndex = 1;
        final JdbcType jdbcType = JdbcType.VARCHAR;

        // Act
        // The method under test is BaseTypeHandler.setParameter.
        stringTypeHandler.setParameter(preparedStatementMock, parameterIndex, null, jdbcType);

        // Assert
        // Verify that BaseTypeHandler correctly calls PreparedStatement.setNull
        // with the appropriate type code when the parameter is null.
        verify(preparedStatementMock).setNull(parameterIndex, jdbcType.TYPE_CODE);

        // Verify that no other interactions with the mock occurred.
        verifyNoMoreInteractions(preparedStatementMock);
    }
}