package org.ivcode.homeportal.repositories

import org.apache.ibatis.annotations.*
import org.ivcode.homeportal.repositories.entities.LinkEntity
import org.ivcode.homeportal.repositories.entities.LinkIconEntity

private const val INSERT_LINK = """
    INSERT INTO link (image_id, name, href)
    VALUES (#{imageId}, #{name}, #{href})
"""

private const val INSERT_LINK_ROLES = """<script>
    INSERT INTO link_role (link_id, role_name)
    VALUES
    <foreach item="role" collection="roles" separator=",">
        (#{linkId}, #{role})
    </foreach>
</script>"""

private const val UPDATE_LINK = """
    UPDATE link
    SET image_id=#{link.imageId}, name=#{link.name}, href=#{link.href}
    WHERE name=#{oldName}
"""

private const val CLEAR_LINK_IMAGE = """
    UPDATE link SET image_id=NULL WHERE name=#{name}
"""

private const val DELETE_LINK_ROLES = """
    DELETE FROM link_role WHERE link_id=#{linkId}
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

private const val DELETE_ALL_LINKS = """
    DELETE FROM link
"""

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

private const val SELECT_LINK_ROLES = """
    SELECT role_name FROM link_role WHERE link_id=#{linkId} ORDER BY role_name
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
    @Result(property = "link.roles", column = "link_id", many = Many(select = "getLinkRoles"))
    public fun getLinkIcons(): List<LinkIconEntity>

    @Select(SELECT_LINK_ICONS_BY_NAME)
    @Result(property = "link.id", column = "link_id")
    @Result(property = "link.name", column = "link_name")
    @Result(property = "link.href", column = "link_href")
    @Result(property = "image.id", column = "image_id")
    @Result(property = "image.mime", column = "image_mime")
    @Result(property = "image.path", column = "image_path")
    @Result(property = "image.filename", column = "image_filename")
    @Result(property = "link.roles", column = "link_id", many = Many(select = "getLinkRoles"))
    public fun getLinkIconsByName(names: List<String>): List<LinkIconEntity>

    @Select(SELECT_LINK_ICON)
    @Result(property = "link.id", column = "link_id")
    @Result(property = "link.name", column = "link_name")
    @Result(property = "link.href", column = "link_href")
    @Result(property = "image.id", column = "image_id")
    @Result(property = "image.mime", column = "image_mime")
    @Result(property = "image.path", column = "image_path")
    @Result(property = "image.filename", column = "image_filename")
    @Result(property = "link.roles", column = "link_id", many = Many(select = "getLinkRoles"))
    public fun getLinkIcon(name: String): LinkIconEntity?

    @Select(SELECT_LINK_ROLES)
    public fun getLinkRoles(@Param("linkId") linkId: Long): List<String>

    @Insert(INSERT_LINK)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public fun createLink(link: LinkEntity): Int

    @Insert(INSERT_LINK_ROLES)
    public fun createLinkRoles(@Param("linkId") linkId: Long, @Param("roles") roles: List<String>): Int

    @Update(UPDATE_LINK)
    public fun updateLink(@Param("oldName") oldName: String, @Param("link") link: LinkEntity): Int

    @Update(CLEAR_LINK_IMAGE)
    public fun clearLinkImage(name: String): Int

    @Delete(DELETE_LINK_ROLES)
    public fun deleteLinkRoles(linkId: Long): Int

    @Delete(DELETE_LINK)
    public fun deleteLink(name: String): Int

    @Delete(DELETE_LINKS_BY_ID)
    public fun deleteLinksById(linkIds: List<Long>): Int

    /** Deletes every row in the link table. Used to reset state before restoring a backup. */
    @Delete(DELETE_ALL_LINKS)
    public fun deleteAllLinks(): Int
}
