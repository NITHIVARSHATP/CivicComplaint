from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

# This connects to your friend's DB: Civic-Complaint-Management
CIVIC_DB_URL = "postgresql://postgres:mukhilplsql@localhost:5432/Civic-Complaint-Management"

civic_engine = create_engine(CIVIC_DB_URL)
CivicSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=civic_engine)

CivicBase = declarative_base()
