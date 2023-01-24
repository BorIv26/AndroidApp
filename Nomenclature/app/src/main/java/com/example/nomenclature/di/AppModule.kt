package com.example.nomenclature.di

import android.content.Context
import androidx.datastore.dataStore
import com.example.nomenclature.core.json_rpc_library.data.JsonRpcClientImpl
import com.example.nomenclature.core.json_rpc_library.data.createJsonRpcService
import com.example.nomenclature.core.moshi_json_parser.MoshiRequestConverter
import com.example.nomenclature.core.moshi_json_parser.MoshiResponseParser
import com.example.nomenclature.core.moshi_json_parser.MoshiResultParser
import com.example.nomenclature.data.remote.NomenclatureApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationApi(): NomenclatureApi {
        val okHttpClient = OkHttpClient.Builder().build()

        val jsonRpcClient = JsonRpcClientImpl(
            baseUrl = NomenclatureApi.BASE_URL,
            okHttpClient = okHttpClient,
            requestConverter = MoshiRequestConverter(),
            responseParser = MoshiResponseParser()
        )

        return createJsonRpcService(
            service = NomenclatureApi::class.java,
            client = jsonRpcClient,
            resultParser = MoshiResultParser(),
            interceptors = listOf()
        )
    }
}