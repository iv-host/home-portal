package org.ivcode.homeportal.services

import org.ivcode.homeportal.config.ADMIN_AUTHORITY
import org.ivcode.homeportal.services.models.Features
import org.ivcode.homeportal.services.models.UserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(
    @Value("\${security.oauth2.enabled:false}") private val isOAuth2Enabled: Boolean
) {
    fun getUserInfo(): UserInfo = UserInfo(
        features = Features(
            settings = isAdmin()
        )
    )

    private fun isAdmin():Boolean {
        return !isOAuth2Enabled
                || SecurityContextHolder
                    .getContext()
                    .authentication
                    .authorities.map { a -> a.authority }
                    .contains(ADMIN_AUTHORITY)
    }
}