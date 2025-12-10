from fastapi import FastAPI, Depends
from pydantic import BaseModel
from sqlalchemy.orm import Session

from backend.ai.ai_engine import classify_complaint
from .database import SessionLocal
from .models import ProcessedComplaint

app = FastAPI(title="AI Complaint Backend")

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

class ComplaintRequest(BaseModel):
    complaint_id: int
    text: str

@app.get("/")
def home():
    return {"message": "AI Complaint System Backend Running 🚀"}

@app.post("/process")
def process(data: ComplaintRequest, db: Session = Depends(get_db)):
    result = classify_complaint(data.text)

    entry = ProcessedComplaint(
        complaint_id=data.complaint_id,
        text=data.text,
        category=result["predicted_category"],
        priority=result["priority"],
        confidence=result["confidence_score"]
    )

    db.add(entry)
    db.commit()
    db.refresh(entry)

    return {"message": "Stored successfully", "id": entry.id, "result": result}
