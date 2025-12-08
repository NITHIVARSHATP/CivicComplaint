from transformers import pipeline

# Load model once when file imports
classifier = pipeline(
    "zero-shot-classification",
    model="facebook/bart-large-mnli"
)

#priority setting
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
    
# Fixed categories used in the civic complaint system
CATEGORIES = ["Plumbing", "Electrical", "Waste Management", "Toxic Waste"]

def classify_complaint(text: str) -> dict:
    """
    Takes a complaint text and returns:
    - predicted category
    - confidence score
    - full ranking of all category probabilities
    """
    
    result = classifier(text, CATEGORIES)

    priority = assign_priority_keywords(text)

    return {
        "text": text,
        "predicted_category": result["labels"][0],
        "confidence": float(result["scores"][0]),
        "priority": priority,
        "all_scores": dict(zip(result["labels"], [float(s) for s in result["scores"]]))
    }




# Optional test
if __name__ == "__main__":
    example = "Street light is sparking"
    print(classify_complaint(example))
