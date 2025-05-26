import 'package:flutter_test/flutter_test.dart';
import 'package:serial_qr_plugin/serial_qr_plugin.dart';
import 'package:serial_qr_plugin/serial_qr_plugin_platform_interface.dart';
import 'package:serial_qr_plugin/serial_qr_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSerialQrPluginPlatform
    with MockPlatformInterfaceMixin
    implements SerialQrPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final SerialQrPluginPlatform initialPlatform = SerialQrPluginPlatform.instance;

  test('$MethodChannelSerialQrPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSerialQrPlugin>());
  });

  test('getPlatformVersion', () async {
    SerialQrPlugin serialQrPlugin = SerialQrPlugin();
    MockSerialQrPluginPlatform fakePlatform = MockSerialQrPluginPlatform();
    SerialQrPluginPlatform.instance = fakePlatform;


  });
}
