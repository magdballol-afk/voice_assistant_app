import 'package:speech_to_text/speech_to_text.dart' as stt;
import 'package:flutter_tts/flutter_tts.dart';

class VoiceService {
  final stt.SpeechToText _speech = stt.SpeechToText();
  final FlutterTts _tts = FlutterTts();
  bool _isListening = false;

  bool get isListening => _isListening;

  // تهيئة المحرك الصوتي والنطق
  Future<bool> initVoice() async {
    await _tts.setLanguage("ar-SA"); // ضبط اللغة العربية
    await _tts.setSpeechRate(0.45);  // السرعة تناسب ذوي الاحتياجات الخاصة
    await _tts.setPitch(1.0);

    bool available = await _speech.initialize(
      onError: (val) => print('Error: $val'),
      onStatus: (val) => print('Status: $val'),
    );
    return available;
  }

  // التحدث بصوت عالي
  Future<void> speak(String text) async {
    await _tts.stop();
    await _tts.speak(text);
  }

  // بدء الاستماع للأوامر
  Future<void> listen(Function(String) onResult) async {
    if (!_isListening) {
      bool available = await _speech.initialize();
      if (available) {
        _isListening = true;
        _speech.listen(
          localeId: "ar_SA",
          onResult: (val) {
            if (val.finalResult) {
              _isListening = false;
              onResult(val.recognizedWords);
            }
          },
        );
      }
    }
  }

  // إيقاف الاستماع
  void stop() {
    _speech.stop();
    _isListening = false;
  }
}
