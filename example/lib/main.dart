import 'package:flutter/material.dart';
import 'package:serial_qr_plugin/serial_qr_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(home: SerialReaderWidget());
  }
}

class SerialReaderWidget extends StatefulWidget {
  @override
  _SerialReaderWidgetState createState() => _SerialReaderWidgetState();
}

class _SerialReaderWidgetState extends State<SerialReaderWidget> {
  String lastData = "Henüz veri yok";

  @override
  void initState() {
    super.initState();
    SerialQrPlugin.qrStream.listen((data) {
      print("QR: $data");
    });

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("QR Seri Okuyucu")),
      body: Center(child: Text("Gelen Veri:\n$lastData")),
    );
  }
}
