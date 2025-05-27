package com.example.serial_qr_plugin
import android.os.Handler
import android.os.Looper
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import java.io.File
import java.io.FileInputStream

class SerialQrPlugin : FlutterPlugin, EventChannel.StreamHandler {
  private var eventSink: EventChannel.EventSink? = null
  private var running = false

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    val eventChannel = EventChannel(binding.binaryMessenger, "serial_qr_plugin/events")
    eventChannel.setStreamHandler(this)
  }

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    eventSink = events
    running = true
    startSerialReading()
  }

  override fun onCancel(arguments: Any?) {
    running = false
    eventSink = null
  }

  private fun startSerialReading() {
    Thread {
      val file = File("/dev/ttyS4") // veya cihazına göre ttyUSB0 / ttyACM0
      if (!file.exists()) {
        Handler(Looper.getMainLooper()).post {
          eventSink?.error("PORT_NOT_FOUND", "Port bulunamadı: /dev/ttyS1", null)
        }
        return@Thread
      }

      try {
        val inputStream = FileInputStream(file)
        val buffer = ByteArray(1024)
        var accumulated = ""

        while (running) {
          val bytesRead = inputStream.read(buffer)
          if (bytesRead > 0) {
            val newData = String(buffer, 0, bytesRead, Charsets.UTF_8)
            accumulated += newData

            // Eğer bir QR okuma tamamlandıysa (64 karakter örnek verdin)
            if (accumulated.length >= 64) {
              val qrCode = accumulated.substring(0, 64)
              accumulated = accumulated.removePrefix(qrCode)

              Handler(Looper.getMainLooper()).post {
                eventSink?.success(qrCode)
              }
            }
          } else {
            Thread.sleep(10)
          }
        }

        inputStream.close()
      } catch (e: Exception) {
        Handler(Looper.getMainLooper()).post {
          eventSink?.error("READ_ERROR", e.message, null)
        }
      }
    }.start()
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {}
}