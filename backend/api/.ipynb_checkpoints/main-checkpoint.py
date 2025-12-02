from fastapi import FastAPI
from pydantic import BaseModel
from backend.ai.ai_engine import classify_complaint

app = FastAPI(title="Civic Complaint AI System")

class ComplaintRequest(BaseModel):
    text: str

@app.get("/")
def home():
    return {"message": "Civic Complaint AI API is running 🚀"}

@app.post("/predict")
def predict(data: ComplaintRequest):
    result = classify_complaint(data.text)
    return result
