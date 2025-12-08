// config/global_config.dart

import 'dart:io' show Platform;
import 'package:flutter/foundation.dart';

/// Global configuration settings for the application.
class GlobalConfig {
  // Port for Spring Boot
  static const _port = '8090';
  // Default host for Web, iOS Simulator, Desktop
  static const _defaultHost = 'localhost';
  // Special alias for the host machine from the Android Emulator
  static const _androidHost = '10.0.2.2';
  // The API path prefix
  static const _apiPath = '/api';

  /// Returns the correct base URL based on the running platform.
  static String get baseUrl {
    String host;

    if (kIsWeb) {
      host = _defaultHost;
    } else if (Platform.isAndroid) {
      host = _androidHost;
    } else if (Platform.isIOS) {
      host = _defaultHost; // iOS simulator
    } else {
      host = _defaultHost; // desktop, etc.
    }

    return 'http://10.0.2.2:8090/api';
  }
}
