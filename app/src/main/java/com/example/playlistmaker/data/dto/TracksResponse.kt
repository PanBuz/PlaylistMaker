package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.Track

class TracksResponse (val resultCount: Int,
                      val results:  ArrayList<Track>)