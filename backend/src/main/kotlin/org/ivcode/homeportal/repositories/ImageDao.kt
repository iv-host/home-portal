package org.ivcode.homeportal.repositories

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select
import org.ivcode.homeportal.repositories.entities.ImageMetaEntity
import java.io.InputStream

private const val SELECT_META_DATA = """
    SELECT id, mime, path, filename
    FROM image
    WHERE path=#{path} AND filename=#{filename}
"""

private const val SELECT_DATA = """
    SELECT data
    FROM image
    WHERE path=#{path} AND filename=#{filename}
"""

private const val CREATE_IMAGE = """
    INSERT INTO image (path, filename, mime, data)
    VALUES (#{metaData.path}, #{metaData.filename}, #{metaData.mime}, #{data})
"""

private const val DELETE_IMAGE = """
    DELETE FROM image
    WHERE path=#{path} AND filename=#{filename}
"""

private const val DELETE_IMAGES_BY_ID = """<script>
    DELETE FROM image
    WHERE
        <foreach item="id" index="index" collection="imageIds" open="id IN (" separator="," close=")" nullable="true">
            #{id}
        </foreach>
</script>"""

@Mapper
public interface ImageDao {

    @Select(SELECT_META_DATA)
    public fun getMetaData(path: String, filename: String): ImageMetaEntity?

    @Select(SELECT_DATA)
    public fun getData(path: String, filename: String): InputStream?

    @Insert(CREATE_IMAGE)
    @Options(useGeneratedKeys = true, keyProperty = "metaData.id", keyColumn = "id")
    public fun createImage(metaData: ImageMetaEntity, data: InputStream): Int

    @Delete(DELETE_IMAGE)
    public fun deleteImage(path: String, filename: String): Int

    @Delete(DELETE_IMAGES_BY_ID)
    public fun deleteImagesById(imageIds: List<Long>): Int
}