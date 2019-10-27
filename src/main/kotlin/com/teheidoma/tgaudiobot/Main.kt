package com.teheidoma.tgaudiobot

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.TelegramBotsApi
import java.util.*

val prop = PropertiesReader()
val mainLogger = LoggerFactory.getLogger("main")

fun main() {
    initBot()
}

fun initBot(){
    ApiContextInitializer.init()
    val api = TelegramBotsApi()
    api.registerBot(AudioBot(prop))
}