package org.ivcode.homeportal.api

import org.ivcode.homeportal.api.models.OAuth2TokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface OAuth2TokenApi {
    @POST(".")
    @FormUrlEncoded
    fun token (
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String? = null,
        @Field("client_id") clientId: String? = null,
        @Field("client_secret") clientSecret: String? = null,
    ): Call<OAuth2TokenResponse>

    @POST(".")
    @FormUrlEncoded
    fun refresh (
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("scope") scope: String? = null,
        @Field("client_id") clientId: String? = null,
        @Field("client_secret") clientSecret: String? = null,
    ): Call<OAuth2TokenResponse>

}