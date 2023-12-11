package org.ivcode.homeportal.repositories

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select
import org.ivcode.homeportal.repositories.entities.ImageMetaEntity
import java.io.InputStream

const val SELECT_META_DATA = """
    SELECT id, mime, path, filename
    FROM image
    WHERE path=#{path} AND filename=#{filename}
"""

const val SELECT_DATA = """
    SELECT data
    FROM image
    WHERE path=#{path} AND filename=#{filename}
"""

const val CREATE_IMAGE = """
    INSERT INTO image (path, filename, mime, data)
    VALUES (#{metaData.path}, #{metaData.filename}, #{metaData.mime}, #{data})
"""

const val DELETE_IMAGE = """
    DELETE FROM image
    WHERE path=#{path} AND filename=#{filename}
"""

@Mapper
interface ImageDao {

    @Select(SELECT_META_DATA)
    fun getMetaData(path: String, filename: String): ImageMetaEntity?

    @Select(SELECT_DATA)
    fun getData(path: String, filename: String): InputStream?

    @Insert(CREATE_IMAGE)
    @Options(useGeneratedKeys = true, keyProperty = "metaData.id", keyColumn = "id")
    fun createImage(metaData: ImageMetaEntity, data: InputStream): Int

    @Delete(DELETE_IMAGE)
    fun deleteImage(path: String, filename: String): Int
}