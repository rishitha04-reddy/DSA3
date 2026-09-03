# TextHack Workflow and Processing Components

print("========== TEXTHACK WORKFLOW ==========")

steps = [
    "1. Input Text / Document",
    "2. Text Preprocessing",
    "3. Pattern Searching",
    "4. Similarity Analysis",
    "5. Result Generation"
]

for step in steps:
    print(step)

print("\n========== PROCESSING COMPONENTS ==========")

components = {
    "Input Module": "Accepts text or documents",
    "Preprocessing Module": "Cleans and prepares the text",
    "Search Module": "Searches for patterns or keywords",
    "Similarity Module": "Compares text or documents",
    "Output Module": "Displays the final results"
}

for component, description in components.items():
    print(component, ":", description)
