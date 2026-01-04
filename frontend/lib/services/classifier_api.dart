import 'dart:convert';
import 'package:http/http.dart' as http;

class ClassifierApi {
  static const String _baseUrl = "http://10.0.2.2:8000/predict";

  static Future<Map<String, dynamic>> classifyComplaint(String text) async {
    try {
      final response = await http.post(
        Uri.parse(_baseUrl),
        headers: {"Content-Type": "application/json"},
        body: jsonEncode({"text": text}),
      );

      if (response.statusCode == 200) {
        return jsonDecode(response.body);
      } else {
        throw Exception("Model returned status: ${response.statusCode}");
      }
    } catch (e) {
      throw Exception("Error connecting to AI model: $e");
    }
  }
}
