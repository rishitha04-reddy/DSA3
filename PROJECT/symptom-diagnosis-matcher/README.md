# Symptom-to-Diagnosis Matcher

Based on the DSA-3 project abstract.

## Stack
- Java 17+
- Built-in Java HTTP server (no external dependency)
- HTML/CSS/JavaScript frontend
- DSA in Java: HashMap, HashSet, ArrayList, searching, sorting, scoring/ranking

## Run in VS Code

1. Install JDK 17 or above.
2. Open this folder in VS Code.
3. Open Terminal.
4. Run:

```bash
javac -d out src/Main.java
java -cp out Main
```

5. Open:
http://localhost:8080

## Important project behavior

This is an educational decision-support prototype, not a medical diagnosis system.
It deliberately does NOT claim that any arbitrary symptom combination can be diagnosed with certainty.

The knowledge base contains a set of common conditions and symptom synonyms. Free text is normalized, matched using HashMap/HashSet structures, scored, and ranked.

For unknown symptoms, the system can return insufficient matching information instead of inventing a disease.

## Extending the project

Add more canonical symptoms and synonyms in `Main.java`, then add more `disease(...)` records. For a final academic version, the knowledge base can be moved to MySQL using JDBC, matching the original abstract's database requirement.
