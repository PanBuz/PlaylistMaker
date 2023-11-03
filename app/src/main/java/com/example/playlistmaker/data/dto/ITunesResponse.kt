package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.Track

class ITunesResponse (  val resultCount: Int,
                        var results : ArrayList<Track>)