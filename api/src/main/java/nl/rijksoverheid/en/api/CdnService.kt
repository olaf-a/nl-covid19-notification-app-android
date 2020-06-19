/*
 * Copyright (c) 2020 De Staat der Nederlanden, Ministerie van Volksgezondheid, Welzijn en Sport.
 *  Licensed under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 *
 *  SPDX-License-Identifier: EUPL-1.2
 */
package nl.rijksoverheid.en.api

import android.content.Context
import nl.rijksoverheid.en.api.model.AppConfig
import nl.rijksoverheid.en.api.model.Manifest
import nl.rijksoverheid.en.api.model.RiskCalculationParameters
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface CdnService {
    @GET("v1/exposurekeyset/{id}")
    @Accept("application/zip")
    @Streaming
    suspend fun getExposureKeySetFile(@Path("id") id: String): Response<ResponseBody>

    @GET("v1/manifest")
    @Accept(mimeType = BuildConfig.CDN_RESPONSE_MIME_TYPE)
    suspend fun getManifest(): Manifest

    @GET("v1/riskcalculationparameters/{id}")
    @Accept(mimeType = BuildConfig.CDN_RESPONSE_MIME_TYPE)
    suspend fun getRiskCalculationParameters(@Path("id") id: String): RiskCalculationParameters

    @GET("v1/appconfig/{id}")
    @Accept(mimeType = BuildConfig.CDN_RESPONSE_MIME_TYPE)
    suspend fun getAppConfig(@Path("id") id: String): AppConfig

    companion object {
        fun create(
            context: Context,
            client: OkHttpClient = createOkHttpClient(context),
            baseUrl: String = BuildConfig.CDN_BASE_URL
        ): CdnService {
            return Retrofit.Builder()
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
                .baseUrl(baseUrl)
                .build().create(CdnService::class.java)
        }
    }
}