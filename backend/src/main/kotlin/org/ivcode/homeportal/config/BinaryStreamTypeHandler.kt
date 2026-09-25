package org.ivcode.homeportal.config

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedTypes
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Reads/writes binary column data using [java.sql.PreparedStatement.setBinaryStream] and
 * [java.sql.ResultSet.getBinaryStream] instead of the JDBC BLOB APIs.
 *
 * MyBatis' default handler for [InputStream] ([org.apache.ibatis.type.BlobInputStreamTypeHandler]) uses
 * `setBlob`/`getBlob`, which the PostgreSQL driver maps to large-object (OID) semantics rather than the raw
 * bytes stored in a `bytea` column. Using the binary stream APIs instead works consistently across
 * MySQL (`longblob`), H2 (`blob`), and PostgreSQL (`bytea`).
 */
@MappedTypes(InputStream::class)
public class BinaryStreamTypeHandler : BaseTypeHandler<InputStream>() {

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: InputStream, jdbcType: JdbcType?) {
        ps.setBinaryStream(i, parameter)
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): InputStream? =
        rs.getBinaryStream(columnName)

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): InputStream? =
        rs.getBinaryStream(columnIndex)

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): InputStream? =
        cs.getBytes(columnIndex)?.let { ByteArrayInputStream(it) }
}
