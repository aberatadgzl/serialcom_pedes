import 'package:flutter/services.dart';
import 'package:flutter/foundation.dart';

class SerialQrPlugin {
  static const EventChannel _eventChannel =
  EventChannel('serial_qr_plugin/events');

  static Stream<String> get serialStream {
    return _eventChannel
        .receiveBroadcastStream()
        .map((event) => event.toString());
  }
}