import 'package:flutter/material.dart';

enum ComplaintStatus { Submitted, InProgress, Resolved }

class Complaint {
  final String id;
  final String title;
  final String description;
  final String department;
  final ComplaintStatus status;
  final String priority;
  final String submittedByUserId;
  final DateTime submittedDate;
  final String? imageUrl;
  final double latitude;
  final double longitude;

  Complaint({
    required this.id,
    required this.title,
    required this.description,
    required this.department,
    required this.status,
    required this.priority,
    required this.submittedDate,
    required this.submittedByUserId,
    this.imageUrl,
    required this.latitude,
    required this.longitude,
  });

  // ---------- helpers ----------
  static ComplaintStatus _parseStatus(dynamic v) {
    if (v == null) return ComplaintStatus.Submitted;
    final s = v.toString().split('.').last.toLowerCase(); // handle enums and plain strings
    return ComplaintStatus.values.firstWhere(
          (e) => e.toString().split('.').last.toLowerCase() == s,
      orElse: () => ComplaintStatus.Submitted,
    );
  }

  static DateTime _parseDate(Map<String, dynamic> json) {
    // try multiple keys (createdAt, submittedDate)
    final val = (json['createdAt'] ?? json['submittedDate'] ?? json['created_at']);
    if (val == null) return DateTime.now(); // fallback
    try {
      return DateTime.parse(val.toString());
    } catch (_) {
      return DateTime.now();
    }
  }

  static double _parseDouble(dynamic v, [double fallback = 0.0]) {
    if (v == null) return fallback;
    if (v is double) return v;
    if (v is int) return v.toDouble();
    if (v is String) {
      return double.tryParse(v) ?? fallback;
    }
    return fallback;
  }

  static String _safeString(dynamic v, [String fallback = ""]) {
    if (v == null) return fallback;
    return v.toString();
  }

  // ---------- factory ----------
  factory Complaint.fromJson(Map<String, dynamic> json) {
    // id: accept int or string, default ""
    final id = json['id']?.toString() ?? "";

    // title/description/department: default to empty string when null
    final title = _safeString(json['title'], "");
    final description = _safeString(json['description'], "");
    final department = _safeString(json['department'], "");

    // status: parse case-insensitively (server may send 'SUBMITTED')
    final status = _parseStatus(json['status']);

    // priority: keep as string fallback to empty
    final priority = _safeString(json['priority'], "");

    // submittedDate: check createdAt or submittedDate keys
    final submittedDate = _parseDate(json);

    // submittedByUserId: if not present, try submittedByUserName (your logs show that)
    final submittedByUserId = json['submittedByUserId']?.toString() ??
        json['submittedByUserName']?.toString() ??
        "";

    // imageUrl: if null -> keep null (your model defines imageUrl as nullable)
    final imageUrl = json['imageUrl'] == null ? null : json['imageUrl'].toString();

    // latitude / longitude: parse numbers robustly
    final latitude = _parseDouble(json['latitude'], 0.0);
    final longitude = _parseDouble(json['longitude'], 0.0);

    return Complaint(
      id: id,
      title: title,
      description: description,
      department: department,
      status: status,
      priority: priority,
      submittedDate: submittedDate,
      submittedByUserId: submittedByUserId,
      imageUrl: imageUrl,
      latitude: latitude,
      longitude: longitude,
    );
  }

  // 👉 toJson stays mostly the same
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'department': department,
      'status': status.toString().split('.').last,
      'priority': priority,
      'submittedDate': submittedDate.toIso8601String(),
      'submittedByUserId': submittedByUserId,
      'imageUrl': imageUrl,
      'latitude': latitude,
      'longitude': longitude,
    };
  }

  // Helper to get color based on status
  Color get statusColor {
    switch (status) {
      case ComplaintStatus.Submitted:
        return Colors.blue;
      case ComplaintStatus.InProgress:
        return Colors.orange;
      case ComplaintStatus.Resolved:
        return Colors.green;
    }
  }

  // Helper to get icon based on department
  IconData get departmentIcon {
    switch (department) {
      case 'Water Supply Board':
        return Icons.water_damage;
      case 'Electrical':
        return Icons.flash_on;
      case 'Waste Disposal':
        return Icons.delete;
      case 'Roads & Infrastructure':
        return Icons.edit_road;
      default:
        return Icons.report_problem;
    }
  }
}
