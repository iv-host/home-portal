package org.ivcode.homeportal.repositories

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

const val SELECT_1 = "SELECT 1"
@Mapper
interface HealthDao {

    @Select(SELECT_1)
    fun healthCheck(): Int

}