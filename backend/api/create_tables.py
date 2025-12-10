from .database import engine, Base
from . import models  

# Create all tables
Base.metadata.create_all(bind=engine)

print("✔ PostgreSQL tables created successfully!")
