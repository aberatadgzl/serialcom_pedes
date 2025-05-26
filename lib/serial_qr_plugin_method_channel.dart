import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'serial_qr_plugin_platform_interface.dart';

/// An implementation of [SerialQrPluginPlatform] that uses method channels.
class MethodChannelSerialQrPlugin extends SerialQrPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('serial_qr_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
