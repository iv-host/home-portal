package org.ivcode.homeportal.repositories

import org.apache.ibatis.annotations.*
import org.ivcode.homeportal.repositories.entities.BackgroundEntity
import org.ivcode.homeportal.repositories.entities.BackgroundImageEntity

private const val SELECT_BACKGROUND_IMAGES = """
    SELECT
        background.id AS background_id,
        image.id AS image_id,
        image.mime AS image_mime,
        image.path AS image_path,
        image.filename AS image_filename
    FROM
        background INNER JOIN image ON background.image_id=image.id
"""

private const val INSERT_BACKGROUND = """
    INSERT INTO background (image_id)
    VALUES (#{imageId})
"""

private const val DELETE_BACKGROUND_IMAGE = """
    DELETE background, image
    FROM background INNER JOIN image ON background.image_id=image.id
    WHERE image.filename = #{filename}
"""

@Mapper
public interface BackgroundDao {

    @Select(SELECT_BACKGROUND_IMAGES)
    @Result(property = "id", column = "background_id")
    @Result(property = "image.id", column = "image_id")
    @Result(property = "image.mime", column = "image_mime")
    @Result(property = "image.path", column = "image_path")
    @Result(property = "image.filename", column = "image_filename")
    public fun selectBackgroundImages(): List<BackgroundImageEntity>

    @Insert(INSERT_BACKGROUND)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public fun createBackgroundImage(background: BackgroundEntity): Int

    @Delete(DELETE_BACKGROUND_IMAGE)
    public fun deleteBackgroundImage(filename: String): Int
}