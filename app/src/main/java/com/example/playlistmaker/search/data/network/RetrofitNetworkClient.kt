package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest


class RetrofitNetworkClient(
    private val iTunesService: ITunesSearchApi,
    private val context: Context
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        try {
            if (!isOnline(context)) {
                return Response().apply { resultCode = -1 }
            }
            Log.d ("PAN_Retrofit", "dto (${dto.toString()})")
            if (dto is TracksSearchRequest) {
                val response = iTunesService.search(dto.expression).execute()
                Log.d ("PAN_Retrofit", "response (${response.toString()})")
                val body = response.body() ?: Response()
                Log.d ("PAN_Retrofit", "body (${body.toString()})")
                return body.apply { resultCode = response.code()
                    Log.d ("PAN_Retrofit", "resultCode (${resultCode.toString()})")
                }
            } else {
                return Response().apply { resultCode = 400
                }
            }
        }
        catch (exception: Exception) {
            throw exception
            Log.e ("PAN_Retrofit", "Exception ($exception)")

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