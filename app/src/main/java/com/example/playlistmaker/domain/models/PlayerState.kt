package com.example.playlistmaker.domain.models

/*enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED;

}*/

class PlayerState {
    companion object  {
        val STATE_DEFAULT = 0
        val STATE_PREPARED = 1
        val STATE_PLAYING = 2
        val STATE_PAUSED = 3
    }
}