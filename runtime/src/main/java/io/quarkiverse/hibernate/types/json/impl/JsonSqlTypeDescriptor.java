package io.quarkiverse.hibernate.types.json.impl;

import java.sql.*;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.internal.SessionImpl;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class JsonSqlTypeDescriptor implements SqlTypeDescriptor {

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public boolean canBeRemapped() {
        return true;
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            private AbstractJsonSqlTypeDescriptor delegate;

            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(delegate(options).extractJson(rs, name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(delegate(options).extractJson(statement, index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(delegate(options).extractJson(statement, name), options);
            }

            private AbstractJsonSqlTypeDescriptor delegate(WrapperOptions options) {
                if (delegate == null) {
                    delegate = resolveSqlTypeDescriptor(options);
                }
                return delegate;
            }
        };
    }

    @Override
    public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicBinder<X>(javaTypeDescriptor, this) {
            private ValueBinder<X> delegate;

            @Override
            protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
                delegate(options).bind(st, value, index, options);
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {
                delegate(options).bind(st, value, name, options);
            }

            private ValueBinder<X> delegate(WrapperOptions options) {
                if (delegate == null) {
                    delegate = resolveSqlTypeDescriptor(options).getBinder(javaTypeDescriptor);
                }
                return delegate;
            }
        };
    }

    private static AbstractJsonSqlTypeDescriptor resolveSqlTypeDescriptor(WrapperOptions options) {
        SessionImpl session = (SessionImpl) options;
        Dialect dialect = session.getJdbcServices().getDialect();

        if (dialect instanceof PostgreSQL81Dialect) {
            return JsonBinarySqlTypeDescriptor.INSTANCE;
        } else if (dialect instanceof H2Dialect) {
            return JsonBytesSqlTypeDescriptor.INSTANCE;
        } else {
            return JsonStringSqlTypeDescriptor.INSTANCE;
        }
    }

}
