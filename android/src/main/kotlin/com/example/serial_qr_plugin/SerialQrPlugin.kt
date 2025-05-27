package com.example.serial_qr_plugin

import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.File
import java.io.FileInputStream
import kotlin.concurrent.thread

class SerialQrPlugin(private val eventChannel: EventChannel) : EventChannel.StreamHandler {

  private var eventSink: EventChannel.EventSink? = null
  private var running = false
  private var accumulated = ""

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    eventSink = events
    running = true

    val serialPortFile = File("/dev/ttyS4")
    if (!serialPortFile.exists() || !serialPortFile.canRead()) {
      eventSink?.error("FILE_ERROR", "/dev/ttyS4 okunamıyor", null)
      return
    }

    thread(start = true) {
      try {
        val inputStream = FileInputStream(serialPortFile)
        val buffer = ByteArray(1024)

        while (running) {
          val bytesRead = inputStream.read(buffer)
          if (bytesRead > 0) {
            val newData = String(buffer, 0, bytesRead, Charsets.UTF_8)
            accumulated += newData

            while (accumulated.length >= 64) {
              val qr = accumulated.substring(0, 64)
              accumulated = accumulated.substring(64)

              Handler(Looper.getMainLooper()).post {
                eventSink?.success(qr)
              }
            }
          } else {
            Thread.sleep(10)
          }
        }

        inputStream.close()
      } catch (e: Exception) {
        Handler(Looper.getMainLooper()).post {
          eventSink?.error("READ_ERROR", "ttyS4 okumada hata: ${e.message}", null)
        }
        running = false
      }
    }
  }

  override fun onCancel(arguments: Any?) {
    running = false
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = EventChannel(registrar.messenger(), "my_qr_plugin")
      channel.setStreamHandler(SerialQrPlugin(channel))
    }
  }
}