@SuppressWarnings("unchecked")
    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
      if (parameter == null) {
        if (jdbcType == null) {
          throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
        }
        try {
          ps.setNull(i, jdbcType.TYPE_CODE);
        } catch (SQLException e) {
          throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
              + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
              + "Cause: " + e, e);
        }
      } else {
        try {
          setNonNullParameter(ps, i, (E) parameter, jdbcType);
        } catch (Exception e) {
          throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . "
              + "Try setting a different JdbcType for this parameter or a different configuration property. "
              + "Cause: " + e, e);
        }
      }
    }