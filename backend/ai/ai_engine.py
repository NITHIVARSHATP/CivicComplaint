from transformers import pipeline

classifier = pipeline(
    "zero-shot-classification",
    model="facebook/bart-large-mnli"
)

HIGH_PRIORITY = ["fire", "gas leak", "sparking", "electrocution", "burst", "explosion"]
MEDIUM_PRIORITY = ["leakage", "overflow", "blocked", "broken", "smell", "dirty", "crack"]

def assign_priority_keywords(text: str) -> str:
    text_lower = text.lower()
    for word in HIGH_PRIORITY:
        if word in text_lower:
            return "High"
    for word in MEDIUM_PRIORITY:
        if word in text_lower:
            return "Medium"
    return "Low"

CATEGORIES = ["Plumbing", "Electrical", "Waste Management", "Toxic Waste"]

def classify_complaint(text: str) -> dict:
    result = classifier(text, CATEGORIES)
    priority = assign_priority_keywords(text)

    return {
        "predicted_category": result["labels"][0],
        "confidence_score": float(result["scores"][0]),
        "priority": priority
    }
