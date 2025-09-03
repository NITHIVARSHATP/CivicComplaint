AI-Powered Civic Complaint Management System

This is a unified, intelligent platform designed to modernize how cities handle civic complaints. It replaces slow, fragmented, and manual processes with a fast, automated, and accountable workflow that connects citizens, industries, and a multi-level government hierarchy on a single platform.

Key Features
AI-Powered Classification: Uses a fine-tuned BERT model to understand the context of complaints for accurate, automated routing to the correct department.

Intelligent Complaint Grouping: Automatically groups duplicate reports of the same issue using geospatial queries to reduce redundant work and measure a problem's real-world impact.

Automated Accountability Engine: A core feature that uses Service-Level Agreements (SLAs) and a scheduled task to automatically escalate unresolved complaints up the chain of command.

Proactive Hotspot Analytics: Employs the DBSCAN algorithm to analyze complaint data and identify recurring problem areas ("hotspots"), enabling a shift from reactive to proactive governance.

Multi-Modal Input: Accepts complaints via text, OCR for image-based text, Voice-to-Text for audio reports, and Computer Vision (YOLO) for direct damage detection.

Role-Based Ecosystem: Provides tailored dashboards and interfaces for all users, including a mobile app for citizens, a specialized web portal for industries, and a multi-level dashboard for government officials.

Tech Stack

Category	Technology

Frontend (Web)	React.js

Frontend (Mobile)	Flutter

Backend	Python 3, FastAPI 
Database	PostgreSQL with PostGIS extension 
AI - NLP	Hugging Face Transformers (BERT) 
AI - Vision/OCR	Ultralytics YOLOv8, OpenCV, Tesseract 
AI - Audio	OpenAI's Whisper

AI - Analytics	Scikit-learn (DBSCAN, TF-IDF)

Automation	APScheduler (for Cron Jobs)

Collaboration	Git & GitHub

Getting Started

Prerequisites

Ensure you have the following installed on your local machine:

Python 3.8+

Node.js and npm

PostgreSQL and pgAdmin

Git
