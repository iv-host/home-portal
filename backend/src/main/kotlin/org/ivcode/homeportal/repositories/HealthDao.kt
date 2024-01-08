package org.ivcode.homeportal.repositories

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

private const val SELECT_1 = "SELECT 1"
@Mapper
public interface HealthDao {

    @Select(SELECT_1)
    public fun healthCheck(): Int

}