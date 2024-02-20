package com.example.ejemploretrofitcompose.network

import com.example.ejemploretrofitcompose.model.Fruta
import com.example.ejemploretrofitcompose.model.Raiz
import com.example.ejemploretrofitcompose.model.RaizGS
import com.example.ejemploretrofitcompose.model.RespuestaPOST
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL =
    "https://www.amiiboapi.com/api/"

/**
 * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object AmiiboApi {
    val retrofitService: AmbiiboAPIInterface by lazy {
        retrofit.create(AmbiiboAPIInterface::class.java)
    }
}

object FrutaAPI {
    val retrofitServiceFruit: FruitAPIInterface by lazy {
        retrofit.create(FruitAPIInterface::class.java)
    }
}

interface AmbiiboAPIInterface {
    @GET("amiibo/")
    suspend fun getAmiibos(@Query("gameseries") gameSeries : String?): Response<Raiz>

    @GET("gameseries")
    suspend fun getGameSeries(): Response<RaizGS>
}


interface FruitAPIInterface {

    @GET("fruit/all")
    @Headers("Accept: application/json")
    suspend fun GetAllFruits(): Response<List<Fruta>>

    @GET("fruit/{nutrition}")
    @Headers("Accept: application/json")
    suspend fun FindByNutrition(@Path("nutrition") nutricion : String,
                                @Query("min") minimo : Double,
                                @Query("max") maximo : Double): Response<List<Fruta>>

    @GET("fruit/all")
    @Headers("Accept: application/json")
    suspend fun CreateFruit(@Body fruit : Fruta): RespuestaPOST
}