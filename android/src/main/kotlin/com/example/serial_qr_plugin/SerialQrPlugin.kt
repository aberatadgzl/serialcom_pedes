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
      val file = File("/dev/ttyS1")  // cihazına göre ayarla
      if (!file.exists()) {
        eventSink?.success("Port bulunamadı: /dev/ttyS4")
        return@Thread
      }

      try {
        val input = FileInputStream(file)
        val buffer = ByteArray(256)

        while (running) {
          val available = input.available()
          if (available > 0) {
            val size = input.read(buffer)
            val data = String(buffer, 0, size)
            Handler(Looper.getMainLooper()).post {
              eventSink?.success(data)
            }
          }
          Thread.sleep(100)  // CPU koruması
        }

        input.close()
      } catch (e: Exception) {
        Handler(Looper.getMainLooper()).post {
          eventSink?.error("ERROR", "Port okuma hatası: ${e.message}", null)
        }
      }
    }.start()
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {}
}