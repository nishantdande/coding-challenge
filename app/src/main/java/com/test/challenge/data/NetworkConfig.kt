package com.test.challenge.data

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Function will provide retrofit instance
 * @param baseURL : provide base url in string
 * @return : retrofit instance
 */
fun Retrofit.Builder.getBuild(baseURL: String) : Retrofit {
    return this.baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(OkHttpClient.Builder().build())
        .build()
}