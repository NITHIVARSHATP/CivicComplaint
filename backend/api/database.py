from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

# Password contains '@' → must be URL encoded
DATABASE_URL = "postgresql://postgres:Nithish%402@localhost:5432/complaints_db"

engine = create_engine(DATABASE_URL)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()
