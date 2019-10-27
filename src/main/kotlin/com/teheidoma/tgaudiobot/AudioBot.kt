package com.teheidoma.tgaudiobot

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.api.methods.send.SendAudio
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.methods.send.SendVideo
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.generics.LongPollingBot
import java.util.concurrent.Executors
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.net.URI
import java.net.URL


class AudioBot(private val prop: PropertiesReader) : TelegramLongPollingBot() {
    val logger = LoggerFactory.getLogger("audio-bot")
    val startMessage = prop.read("startMessage")

    private val executor = Executors.newCachedThreadPool()
    private val downloaderService = DownloaderService()

    init {
        logger.info("created!")
    }

    override fun getBotToken(): String {
        return prop.readOrThrow("token")
    }

    override fun getBotUsername(): String {
        return prop.readOrThrow("username")
    }

    override fun onUpdateReceived(update: Update) {
        executor.submit {
            println(update)
            if (update.hasMessage()) {
                with(update.message) {
                    logger.debug(text)
                    if (isCommand) {
                        when (text) {
                            "/start" -> if (!startMessage.isNullOrBlank()) sendApiMethod(
                                SendMessage(
                                    chatId,
                                    startMessage
                                )
                            )
                            "/author" -> sendApiMethod(SendMessage(chatId, "@teheidoma"))
                        }
                    }else{
                        if (text.startsWith("http")) {
                            val m = SendMessage(chatId, downloaderService.fetchInfo(text).title)
                            m.replyMarkup = keyBoard(text)
                            sendApiMethod(m)
                        }
                    }
                    this
                }
            }else if(update.hasCallbackQuery()){
                logger.info(update.callbackQuery.toString())
                with(update.callbackQuery){
                    val args = data.split(" ")
                    sendApiMethod(AnswerCallbackQuery().setCallbackQueryId(id))
                    when(args[0]){
                        "dl"->{
                            when(args[1]){
                                "v"->{
                                    val dl = downloaderService.downloadVideo(args[2])
                                    sendVideo(
                                        SendVideo()
                                            .setChatId(from.id.toLong())
                                            .setNewVideo(dl)
                                    )
                                    dl.delete()
                                }
                                "a"->{
                                    val dl = downloaderService.downloadAudio(args[2])
                                    sendAudio(
                                        SendAudio()
                                            .setChatId(from.id.toLong())
                                            .setNewAudio(dl)
                                    )
                                    dl.delete()
                                }
                            }
                        }
                    }
                    this
                }
            }
        }
    }

    private fun keyBoard(url:String): InlineKeyboardMarkup {
        val kb = InlineKeyboardMarkup()
        kb.keyboard = listOf(
            listOf(
                InlineKeyboardButton()
                    .setText("audio")
                    .setCallbackData("dl a $url")
            ),
            listOf(
                InlineKeyboardButton()
                    .setText("video")
                    .setCallbackData("dl v $url")
            )
        )
        return kb

    }
}