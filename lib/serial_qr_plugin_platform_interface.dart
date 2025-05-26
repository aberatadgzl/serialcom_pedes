import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'serial_qr_plugin_method_channel.dart';

abstract class SerialQrPluginPlatform extends PlatformInterface {
  /// Constructs a SerialQrPluginPlatform.
  SerialQrPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static SerialQrPluginPlatform _instance = MethodChannelSerialQrPlugin();

  /// The default instance of [SerialQrPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelSerialQrPlugin].
  static SerialQrPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SerialQrPluginPlatform] when
  /// they register themselves.
  static set instance(SerialQrPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
