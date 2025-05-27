import 'package:flutter/services.dart';
import 'package:flutter/foundation.dart';

class SerialQrPlugin {
  static const EventChannel _eventChannel = EventChannel('my_qr_plugin');

  static Stream<String> get qrStream {
    return _eventChannel.receiveBroadcastStream().map((event) => event.toString());
  }
}