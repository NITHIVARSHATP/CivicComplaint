class User {
  final int? id;
  final String fullName;
  final String email;
  final String role; // "CITIZEN", "OFFICIAL", "ADMIN"


  User({
    this.id,
    required this.fullName,
    required this.email,
    required this.role,

  });

  // --- Factory: Create a User from JSON (Backend Response) ---
  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'],
      fullName: json['fullName'] ?? '',
      email: json['email'] ?? '',
      role: json['role'] ?? 'CITIZEN', // Default to CITIZEN if missing

      // If your login endpoint returns a token inside the user object, map it here.
      // If it's in a header, you might handle it separately in the service.

    );
  }

  // --- Method: Convert User to JSON (For sending to API or LocalStorage) ---
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'fullName': fullName,
      'email': email,
      'role': role,

    };
  }

  // --- Helper: Check Role ---
  bool get isOfficial => role == UserRole.official;
  bool get isCitizen => role == UserRole.citizen;
  bool get isAdmin => role == UserRole.admin;

  // --- Helper: CopyWith (Useful for updating state) ---
  User copyWith({
    int? id,
    String? fullName,
    String? email,
    String? role,
    int? departmentId,
    String? token,
  }) {
    return User(
      id: id ?? this.id,
      fullName: fullName ?? this.fullName,
      email: email ?? this.email,
      role: role ?? this.role,

    );
  }
}

// --- Constants for Roles to avoid typos in UI ---
class UserRole {
  static const String citizen = 'CITIZEN';
  static const String official = 'OFFICIAL';
  static const String admin = 'ADMIN';
}