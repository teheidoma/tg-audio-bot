package com.teheidoma.tgaudiobot

import com.sapher.youtubedl.YoutubeDL
import com.sapher.youtubedl.YoutubeDLRequest
import com.sapher.youtubedl.mapper.VideoInfo
import java.io.File
import java.util.*

class DownloaderService {

    init{
        val f = File("downloads/")
        if(!f.exists()) f.mkdir()
    }

    fun fetchInfo(url: String): VideoInfo {
        return YoutubeDL.getVideoInfo(url)
    }

    fun downloadAudio(url:String): File {
        return download(url, "mp3", true)
    }

    fun downloadVideo(url: String): File {
        return download(url, "mp4", false)
    }

    private fun download(url: String, ext:String, isAudio:Boolean): File {
        val name = UUID.randomUUID().toString()
        val req = YoutubeDLRequest(url, "downloads/")

        if(isAudio){
            req.setOption("extract-audio")
            req.setOption("audio-format", ext)
        }else{
            req.setOption("format", ext)
        }
        req.setOption("add-metadata")
        req.setOption("output", "$name.%(ext)s")
        YoutubeDL.execute(req)
        return File("downloads/$name.$ext")
    }
}