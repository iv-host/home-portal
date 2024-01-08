package org.ivcode.homeportal.repositories

import org.apache.ibatis.annotations.*
import org.ivcode.homeportal.repositories.entities.LinkEntity
import org.ivcode.homeportal.repositories.entities.LinkIconEntity

private const val INSERT_LINK = """
    INSERT INTO link (image_id, name, href)
    VALUES (#{imageId}, #{name}, #{href})
"""

private const val DELETE_LINK = """
    DELETE FROM link WHERE name=#{name} 
"""

private const val DELETE_LINKS_BY_ID = """<script>
    DELETE FROM link
    WHERE
        <foreach item="id" index="index" collection="linkIds" open="id IN (" separator="," close=")" nullable="true">
            #{id}
        </foreach>
</script>"""

private const val SELECT_LINK_ICONS = """
    SELECT
        link.id as link_id,
        link.name as link_name,
        link.href as link_href,
        image.id as image_id,
        image.mime as image_mime,
        image.path as image_path,
        image.filename as image_filename
    FROM
        link LEFT JOIN image ON link.image_id=image.id 
"""

private const val SELECT_LINK_ICONS_BY_NAME = """<script>
    SELECT
        link.id as link_id,
        link.name as link_name,
        link.href as link_href,
        image.id as image_id,
        image.mime as image_mime,
        image.path as image_path,
        image.filename as image_filename
    FROM
        link LEFT JOIN image ON link.image_id=image.id
    WHERE
        <foreach item="name" index="index" collection="names" open="link.name IN (" separator="," close=")" nullable="true">
            #{name}
        </foreach>
</script>"""

private const val SELECT_LINK_ICON = """
    SELECT
        link.id as link_id,
        link.name as link_name,
        link.href as link_href,
        image.id as image_id,
        image.mime as image_mime,
        image.path as image_path,
        image.filename as image_filename
    FROM
        link LEFT JOIN image ON link.image_id=image.id
    WHERE
        link.name=#{name}
"""

@Mapper
public interface LinkDao {

    @Select(SELECT_LINK_ICONS)
    @Result(property = "link.id", column = "link_id")
    @Result(property = "link.name", column = "link_name")
    @Result(property = "link.href", column = "link_href")
    @Result(property = "image.id", column = "image_id")
    @Result(property = "image.mime", column = "image_mime")
    @Result(property = "image.path", column = "image_path")
    @Result(property = "image.filename", column = "image_filename")
    public fun getLinkIcons(): List<LinkIconEntity>

    @Select(SELECT_LINK_ICONS_BY_NAME)
    @Result(property = "link.id", column = "link_id")
    @Result(property = "link.name", column = "link_name")
    @Result(property = "link.href", column = "link_href")
    @Result(property = "image.id", column = "image_id")
    @Result(property = "image.mime", column = "image_mime")
    @Result(property = "image.path", column = "image_path")
    @Result(property = "image.filename", column = "image_filename")
    public fun getLinkIconsByName(names: List<String>): List<LinkIconEntity>

    @Select(SELECT_LINK_ICON)
    @Result(property = "link.id", column = "link_id")
    @Result(property = "link.name", column = "link_name")
    @Result(property = "link.href", column = "link_href")
    @Result(property = "image.id", column = "image_id")
    @Result(property = "image.mime", column = "image_mime")
    @Result(property = "image.path", column = "image_path")
    @Result(property = "image.filename", column = "image_filename")
    public fun getLinkIcon(name: String): LinkIconEntity?

    @Insert(INSERT_LINK)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public fun createLink(link: LinkEntity): Int

    @Delete(DELETE_LINK)
    public fun deleteLink(name: String): Int

    @Delete(DELETE_LINKS_BY_ID)
    public fun deleteLinksById(linkIds: List<Long>): Int
}
