package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitNetworkClient(
    private val iTunesService: ITunesSearchApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

            if (!isOnline(context)) {
                return Response().apply { resultCode = -1 }
            }
            Log.d ("PAN_Retrofit", "dto (${dto.toString()})")
            if (dto is TracksSearchRequest) {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = iTunesService.search(dto.expression)
                        response.apply { resultCode = 200 }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }
            } else {
                return Response().apply { resultCode = 400 }
            }

    }
}

private fun isOnline(context: Context): Boolean {
    val transportManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val traficTransport = transportManager.getNetworkCapabilities(transportManager.activeNetwork)
    if (traficTransport != null) {
        if (traficTransport.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("PAN_isOnline", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (traficTransport.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("PAN_isOnline", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (traficTransport.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("PAN_isOnline", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}