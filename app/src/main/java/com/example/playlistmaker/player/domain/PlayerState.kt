package com.example.playlistmaker.player.domain


sealed interface PlayerState {
    object DEFAULT : PlayerState
    object PREPARE : PlayerState
    object PAUSED : PlayerState
    data class PLAYING(val time: Int) : PlayerState

}