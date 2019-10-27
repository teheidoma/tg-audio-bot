package com.teheidoma.tgaudiobot

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.generics.LongPollingBot

class AudioBot(private val prop:PropertiesReader): TelegramLongPollingBot() {
    val logger = LoggerFactory.getLogger("audio-bot")

    init{
        logger.info("created!")
    }

    override fun getBotToken(): String {
        return prop.readOrThrow("token")
    }

    override fun getBotUsername(): String {
        return prop.readOrThrow("username")
    }

    override fun onUpdateReceived(update: Update) {

    }
}