from sqlalchemy import Column, Integer, String, Float
from .database import Base

class ProcessedComplaint(Base):
    __tablename__ = "processed_complaints"

    id = Column(Integer, primary_key=True, index=True)
    complaint_id = Column(Integer)
    text = Column(String)
    category = Column(String)
    priority = Column(String)
    confidence = Column(Float)
    status = Column(String, default="Pending")
