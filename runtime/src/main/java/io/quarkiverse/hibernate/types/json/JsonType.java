package io.quarkiverse.hibernate.types.json;

import java.util.Properties;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import io.quarkiverse.hibernate.types.json.impl.JsonSqlTypeDescriptor;
import io.quarkiverse.hibernate.types.json.impl.JsonTypeDescriptor;

/**
 * <p>
 * {@link JsonType} allows you to map any given JSON object (e.g., POJO, <code>Map&lt;String, Object&gt;</code>, List&lt;T&gt;,
 * <code>JsonNode</code>) on any of the following database systems:
 * </p>
 * <ul>
 * <li><strong>PostgreSQL</strong> - for both <code>jsonb</code> and <code>json</code> column types</li>
 * <li><strong>MySQL</strong> - for the <code>json</code> column type</li>
 * <li><strong>SQL Server</strong> - for the <code>NVARCHAR</code> column type storing JSON</li>
 * <li><strong>Oracle</strong> - for the <code>VARCHAR</code> column type storing JSON</li>
 * <li><strong>H2</strong> - for the <code>json</code> column type</li>
 * </ul>
 *
 * <p>
 * For more details about how to use the {@link JsonType}, check out
 * <a href="https://vladmihalcea.com/how-to-map-json-objects-using-generic-hibernate-types/">this article</a> on
 * <a href="https://vladmihalcea.com/">vladmihalcea.com</a>.
 * </p>
 * <p>
 * If you are using <strong>Oracle</strong> and want to store JSON objects in a <code>BLOB</code> column types, then you should
 * use the {@link JsonBlobType} instead. For more details, check out
 * <a href="https://vladmihalcea.com/oracle-json-jpa-hibernate/">this article</a> on
 * <a href="https://vladmihalcea.com/">vladmihalcea.com</a>.
 * </p>
 *
 * @author Vlad Mihalcea
 */
public class JsonType extends AbstractSingleColumnStandardBasicType<Object> implements DynamicParameterizedType {

    public JsonType() {
        super(new JsonSqlTypeDescriptor(), new JsonTypeDescriptor());
    }

    @Override
    public String getName() {
        return JsonTypes.JSON;
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((JsonTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
