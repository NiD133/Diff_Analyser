package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Tests for the {@link BaseTypeHandler} class.
 * This test suite focuses on the base functionality provided by BaseTypeHandler,
 * using a concrete implementation (ClobTypeHandler) to test non-abstract methods.
 */
public class BaseTypeHandlerTest {

    private static final ClobTypeHandler clobTypeHandler = ClobTypeHandler.INSTANCE;

    /**
     * Verifies that setParameter throws a NullPointerException if the PreparedStatement is null.
     * The underlying JDBC driver requires a non-null PreparedStatement to set a parameter,
     * so the TypeHandler must enforce this contract.
     */
    @Test(expected = NullPointerException.class)
    public void setParameterShouldThrowNullPointerExceptionWhenPreparedStatementIsNull() throws SQLException {
        // The call to setParameter should throw a NullPointerException because the PreparedStatement is null.
        // The other arguments are dummy values required by the method signature.
        // A null parameter is used to ensure the code path that calls ps.setNull() is taken.
        clobTypeHandler.setParameter(null, 1, null, JdbcType.CLOB);
    }
}