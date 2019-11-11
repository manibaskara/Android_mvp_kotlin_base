package com.magnum.app.model.webservice

import android.content.Context
import com.magnum.app.BuildConfig
import com.magnum.app.BuildConfig.BASE_URL
import com.magnum.app.common.Constants.ApiHeaderKey.Companion.AUTHORIZATION
import com.magnum.app.common.Constants.ApiHeaderKey.Companion.PLATFORM
import com.magnum.app.common.Constants.ApiPlatform.Companion.ANDROID
import com.magnum.app.util.sharepreference.SharedPrefsManager
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ApiClient(var context: Context) {

    private var sharedPrefManager: SharedPrefsManager = SharedPrefsManager.create(context)

    fun getClient(): Retrofit {
        var token = ""
        val disposables = CompositeDisposable()
        disposables.add(sharedPrefManager
            .tokenObserver()
            .doOnError { Timber.d(it) }
            .subscribe({
                token = it
            }, {
                Timber.d(it)
            })
        )
        val logging = HttpLoggingInterceptor()

        //setting logging level to basic, so that uploading large files will not result OOM exception
        logging.level = HttpLoggingInterceptor.Level.BASIC   // set your desired log level

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.connectTimeout(2, TimeUnit.MINUTES) //Connection time out set limit
        httpClient.readTimeout(2, TimeUnit.MINUTES)  //Connection read time out set limit

        /*
        * If   ==> token not empty , it will be shared in the header
        * Else ==> It will not add in the header
        * */
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header(PLATFORM, ANDROID)
                .method(original.method(), original.body())
            if (token.isNotEmpty()) {
                requestBuilder.header(AUTHORIZATION, token)
            }
            disposables.clear()
            /*  if (!sharedPrefManager?.token.isNullOrEmpty()) {
                requestBuilder.header(AUTHORIZATION, sharedPrefManager?.token!!)
            }*/
            val request = requestBuilder.build()
            chain.proceed(request)

        }
        // addItem your other interceptors …
        // addItem logging as last interceptor
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(logging)  // <-- this is the important line!
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create()) /*Use can mention your desired parser over here.!*/
            .build()
    }

    private fun getClientWithToken(token: String): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY   // set your desired log level

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.connectTimeout(2, TimeUnit.MINUTES) //Connection time out set limit
        httpClient.readTimeout(2, TimeUnit.MINUTES)  //Connection read time out set limit

        /*
        * If   ==> token not empty , it will be shared in the header
        * Else ==> It will not add in the header
        * */
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header(PLATFORM, ANDROID)
                .method(original.method(), original.body())
            if (token.isNotEmpty()) {
                requestBuilder.header(AUTHORIZATION, token)
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        // addItem your other interceptors …
        // addItem logging as last interceptor
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(logging)  // <-- this is the important line!
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create()) /*Use can mention your desired parser over here.!*/
            .build()
    }

    fun getApiInterface(): ApiInterface = getClient().create(ApiInterface::class.java)

    fun getApiInterfaceWithToken(token: String): ApiInterface =
        getClientWithToken(token).create(ApiInterface::class.java)
}
