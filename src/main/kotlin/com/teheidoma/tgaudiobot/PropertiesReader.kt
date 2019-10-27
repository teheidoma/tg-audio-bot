package com.teheidoma.tgaudiobot

import java.io.File
import java.util.*
import kotlin.system.exitProcess

class PropertiesReader{
    private val file = File("application.properties")
    private val properties = Properties()

    init{
        if(!file.exists()){
            file.createNewFile()
            file.outputStream().use {
                properties["username"]=""
                properties["token"]=""
                properties.store(it, null)
                println("please fill application.properties file ")
                exitProcess(1)
            }
        }

        file.inputStream().use {
            properties.load(it)
        }
        Runtime.getRuntime().addShutdownHook(Thread{
            save()
        })
    }

    fun read(key:String):String?{
        val value = properties[key] as String?
        if(value?.isBlank()!!) {
            return null
        }
        return value
    }
    fun readOrThrow(key: String):String{
        return read(key) ?: throw IllegalArgumentException("$key is not provided!")
    }

    fun write(key:String, value:String){
        properties[key] = value
    }

    fun save(){
        file.outputStream().use {
            properties.store(it, null)
        }
    }
}