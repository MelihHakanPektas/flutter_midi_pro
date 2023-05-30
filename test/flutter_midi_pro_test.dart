import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_midi_pro/flutter_midi_pro.dart';
import 'package:flutter_midi_pro/flutter_midi_pro_platform_interface.dart';
import 'package:flutter_midi_pro/flutter_midi_pro_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterMidiProPlatform
    with MockPlatformInterfaceMixin
    implements FlutterMidiProPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterMidiProPlatform initialPlatform = FlutterMidiProPlatform.instance;

  test('$MethodChannelFlutterMidiPro is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterMidiPro>());
  });

  test('getPlatformVersion', () async {
    FlutterMidiPro flutterMidiProPlugin = FlutterMidiPro();
    MockFlutterMidiProPlatform fakePlatform = MockFlutterMidiProPlatform();
    FlutterMidiProPlatform.instance = fakePlatform;

    expect(await flutterMidiProPlugin.getPlatformVersion(), '42');
  });
}
