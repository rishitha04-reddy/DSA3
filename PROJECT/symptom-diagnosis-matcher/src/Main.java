import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static final Map<String, String> NORMALIZE = new HashMap<>();
    static final List<Disease> DISEASES = new ArrayList<>();
    static final Map<String, List<String>> TESTS = new HashMap<>();

    static {

        // =========================
        // SYMPTOM NORMALIZATION
        // =========================

        addSynonyms("fever",
                "fever", "high temperature", "high temp",
                "temperature", "feeling hot", "pyrexia");

        addSynonyms("cough",
                "cough", "coughing");

        addSynonyms("dry cough",
                "dry cough", "dry coughing", "non productive cough");

        addSynonyms("sore throat",
                "sore throat", "throat pain", "painful throat");

        addSynonyms("runny nose",
                "runny nose", "running nose", "nasal discharge",
                "nose running");

        addSynonyms("blocked nose",
                "blocked nose", "stuffy nose", "nasal congestion",
                "congested nose");

        addSynonyms("sneezing",
                "sneezing", "sneeze");

        addSynonyms("headache",
                "headache", "head pain");

        addSynonyms("body aches",
                "body aches", "body pain", "muscle pain",
                "muscle aches", "body pains");

        addSynonyms("fatigue",
                "fatigue", "tiredness", "extreme tiredness",
                "weakness", "exhaustion");

        addSynonyms("chills",
                "chills", "shivering");

        addSynonyms("shortness of breath",
                "shortness of breath", "breathlessness",
                "difficulty breathing", "breathing difficulty");

        addSynonyms("chest pain",
                "chest pain", "pain in chest");

        addSynonyms("wheezing",
                "wheezing", "whistling breathing");

        addSynonyms("loss of taste or smell",
                "loss of taste", "loss of smell",
                "lost taste", "lost smell");

        addSynonyms("nausea",
                "nausea", "feeling sick", "queasy");

        addSynonyms("vomiting",
                "vomiting", "vomit", "throwing up");

        addSynonyms("diarrhea",
                "diarrhea", "loose stools",
                "loose motion", "watery stools",
                "loose motions");

        addSynonyms("abdominal pain",
                "abdominal pain", "stomach pain",
                "belly pain", "stomach ache");

        addSynonyms("dehydration",
                "dehydration", "very thirsty",
                "dry mouth", "reduced urination");

        addSynonyms("burning urination",
                "burning urination", "burning while urinating",
                "painful urination", "burning pee");

        addSynonyms("frequent urination",
                "frequent urination", "urinating often",
                "pee often", "frequent pee");

        addSynonyms("lower abdominal pain",
                "lower abdominal pain", "lower belly pain");

        addSynonyms("back pain",
                "back pain", "lower back pain");

        addSynonyms("joint pain",
                "joint pain", "painful joints");

        addSynonyms("rash",
                "rash", "skin rash");

        addSynonyms("itchy eyes",
                "itchy eyes", "itching eyes");

        addSynonyms("watery eyes",
                "watery eyes", "teary eyes");

        addSynonyms("heartburn",
                "heartburn", "burning chest after food",
                "acid reflux", "acid in throat");

        addSynonyms("regurgitation",
                "regurgitation", "food coming back up",
                "sour taste");

        addSynonyms("constipation",
                "constipation", "hard stools",
                "difficulty passing stool");

        addSynonyms("bloating",
                "bloating", "bloated stomach",
                "gas and bloating");

        addSynonyms("facial pressure",
                "facial pressure", "pressure in face",
                "face pressure", "sinus pressure");

        addSynonyms("difficulty swallowing",
                "difficulty swallowing", "pain while swallowing",
                "trouble swallowing");

        // =========================
        // DISEASE KNOWLEDGE BASE
        // =========================

        disease(
                "Common Cold",
                "A common respiratory infection usually caused by viruses.",
                "runny nose", "blocked nose", "sneezing",
                "sore throat", "cough", "headache", "fatigue"
        );

        disease(
                "Influenza (Flu)",
                "A viral respiratory infection that commonly causes sudden fever, cough and body aches.",
                "fever", "cough", "sore throat",
                "headache", "body aches", "fatigue", "chills"
        );

        disease(
                "COVID-19",
                "A respiratory infection caused by SARS-CoV-2. Symptoms can vary between people.",
                "fever", "dry cough", "sore throat",
                "fatigue", "headache", "body aches",
                "loss of taste or smell", "shortness of breath"
        );

        disease(
                "Strep Throat",
                "A bacterial throat infection caused by group A Streptococcus.",
                "fever", "sore throat", "headache",
                "chills", "difficulty swallowing"
        );

        disease(
                "Pneumonia",
                "An infection of the lungs that may cause cough, fever and breathing difficulty.",
                "fever", "cough", "shortness of breath",
                "chest pain", "fatigue", "chills"
        );

        disease(
                "Acute Bronchitis",
                "Inflammation of the bronchial tubes that commonly causes cough.",
                "cough", "fatigue", "sore throat",
                "chest pain", "shortness of breath", "wheezing"
        );

        disease(
                "Allergic Rhinitis",
                "An allergic reaction affecting the nose and upper airways.",
                "sneezing", "runny nose", "blocked nose",
                "itchy eyes", "watery eyes"
        );

        disease(
                "Gastroenteritis",
                "Inflammation of the stomach and intestines that can cause vomiting and diarrhea.",
                "nausea", "vomiting", "diarrhea",
                "abdominal pain", "fever", "dehydration"
        );

        disease(
                "Urinary Tract Infection (UTI)",
                "An infection affecting part of the urinary system.",
                "burning urination", "frequent urination",
                "lower abdominal pain", "fever", "back pain"
        );

        disease(
                "Gastroesophageal Reflux Disease (GERD)",
                "A condition where stomach contents frequently flow back into the esophagus.",
                "heartburn", "regurgitation", "sore throat"
        );

        disease(
                "Constipation",
                "Infrequent or difficult bowel movements.",
                "constipation", "abdominal pain", "bloating"
        );

        disease(
                "Migraine",
                "A neurological condition that can cause recurring headaches.",
                "headache", "nausea", "fatigue"
        );

        disease(
                "Chickenpox",
                "A contagious viral infection commonly associated with an itchy rash and fever.",
                "fever", "rash", "fatigue", "headache"
        );

        disease(
                "Dengue",
                "A mosquito-borne viral infection that can cause fever, headache and body aches.",
                "fever", "headache", "body aches",
                "fatigue", "nausea", "rash"
        );

        disease(
                "Viral Fever",
                "A common viral illness that may cause fever, tiredness, headache and body aches.",
                "fever", "headache", "body aches",
                "fatigue", "chills"
        );

        disease(
                "Acute Sinusitis",
                "Inflammation of the sinuses that may cause congestion, facial pressure and headache.",
                "blocked nose", "facial pressure",
                "headache", "sore throat", "fatigue"
        );

        disease(
                "Food Poisoning",
                "Illness caused by contaminated food or drinks.",
                "nausea", "vomiting", "diarrhea",
                "abdominal pain", "fever", "dehydration"
        );

        disease(
                "Asthma",
                "A condition that can cause episodes of wheezing, coughing and breathing difficulty.",
                "wheezing", "shortness of breath",
                "cough", "chest pain"
        );

        disease(
                "Tonsillitis",
                "Inflammation of the tonsils that commonly causes sore throat and difficulty swallowing.",
                "sore throat", "fever", "headache",
                "fatigue", "difficulty swallowing"
        );

        disease(
                "Seasonal Allergies",
                "An allergic reaction that commonly causes sneezing, runny nose and itchy eyes.",
                "sneezing", "runny nose",
                "itchy eyes", "watery eyes", "blocked nose"
        );

        // =========================
        // DIAGNOSTIC TEST DATABASE
        // =========================

        TESTS.put("Common Cold", Arrays.asList(
                "Usually diagnosed from symptoms and medical history",
                "Routine laboratory testing is usually not required"
        ));

        TESTS.put("Influenza (Flu)", Arrays.asList(
                "Rapid Influenza Diagnostic Test (RIDT)",
                "Rapid molecular influenza test",
                "RT-PCR / molecular testing when clinically indicated"
        ));

        TESTS.put("COVID-19", Arrays.asList(
                "COVID-19 antigen test",
                "NAAT / PCR test"
        ));

        TESTS.put("Strep Throat", Arrays.asList(
                "Rapid Group A Streptococcus test",
                "Throat culture"
        ));

        TESTS.put("Pneumonia", Arrays.asList(
                "Physical examination",
                "Chest X-ray",
                "Pulse oximetry",
                "Blood tests when clinically indicated"
        ));

        TESTS.put("Acute Bronchitis", Arrays.asList(
                "Physical examination",
                "Chest X-ray may be considered to rule out pneumonia"
        ));

        TESTS.put("Allergic Rhinitis", Arrays.asList(
                "Clinical examination",
                "Allergy skin testing when needed",
                "Specific IgE blood testing when needed"
        ));

        TESTS.put("Gastroenteritis", Arrays.asList(
                "Clinical examination",
                "Stool testing when infection is suspected",
                "Blood tests when clinically indicated"
        ));

        TESTS.put("Urinary Tract Infection (UTI)", Arrays.asList(
                "Urinalysis",
                "Urine culture",
                "Blood tests if a more serious infection is suspected"
        ));

        TESTS.put("Gastroesophageal Reflux Disease (GERD)", Arrays.asList(
                "Symptoms and medical history",
                "Upper GI endoscopy when indicated",
                "Esophageal pH monitoring when indicated"
        ));

        TESTS.put("Constipation", Arrays.asList(
                "Medical history and physical examination",
                "Blood tests when an underlying condition is suspected",
                "Additional evaluation when clinically indicated"
        ));

        TESTS.put("Migraine", Arrays.asList(
                "Neurological examination",
                "Brain imaging only when clinically indicated"
        ));

        TESTS.put("Chickenpox", Arrays.asList(
                "Clinical examination",
                "Laboratory testing when diagnosis is uncertain"
        ));

        TESTS.put("Dengue", Arrays.asList(
                "NS1 antigen test",
                "Dengue NAAT / RT-PCR",
                "Dengue IgM antibody test"
        ));

        TESTS.put("Viral Fever", Arrays.asList(
                "Clinical examination",
                "Complete blood count (CBC) when clinically indicated",
                "Targeted viral testing depending on symptoms"
        ));

        TESTS.put("Acute Sinusitis", Arrays.asList(
                "Clinical examination",
                "Nasal examination",
                "Imaging for complicated or persistent cases when indicated"
        ));

        TESTS.put("Food Poisoning", Arrays.asList(
                "Clinical examination",
                "Stool testing when infection is suspected",
                "Blood tests when clinically indicated"
        ));

        TESTS.put("Asthma", Arrays.asList(
                "Spirometry",
                "Peak expiratory flow measurement",
                "Bronchodilator response testing"
        ));

        TESTS.put("Tonsillitis", Arrays.asList(
                "Throat examination",
                "Rapid Group A Streptococcus test",
                "Throat culture when indicated"
        ));

        TESTS.put("Seasonal Allergies", Arrays.asList(
                "Clinical examination",
                "Allergy skin-prick testing",
                "Specific IgE blood testing when needed"
        ));
    }

    // =========================
    // DATA STRUCTURES
    // =========================

    static class Disease {
        String name;
        String description;
        Set<String> symptoms;

        Disease(String name, String description, Set<String> symptoms) {
            this.name = name;
            this.description = description;
            this.symptoms = symptoms;
        }
    }

    static class Result {
        String name;
        String description;
        int matched;
        int total;
        double score;
        Set<String> matchedSymptoms;

        Result(String name, String description,
               int matched, int total,
               double score, Set<String> matchedSymptoms) {

            this.name = name;
            this.description = description;
            this.matched = matched;
            this.total = total;
            this.score = score;
            this.matchedSymptoms = matchedSymptoms;
        }
    }

    // =========================
    // NORMALIZATION
    // =========================

    static void addSynonyms(String canonical, String... words) {
        for (String word : words) {
            NORMALIZE.put(word.toLowerCase(), canonical);
        }
    }

    static void disease(
            String name,
            String description,
            String... symptoms) {

        DISEASES.add(
                new Disease(
                        name,
                        description,
                        new HashSet<>(Arrays.asList(symptoms))
                )
        );
    }

    static List<String> normalizeInput(String input) {

        String text = input
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ");

        List<String> keys =
                new ArrayList<>(NORMALIZE.keySet());

        // Searching longer phrases first
        keys.sort(
                (a, b) ->
                        Integer.compare(
                                b.length(),
                                a.length()
                        )
        );

        Set<String> seen = new HashSet<>();
        List<String> found = new ArrayList<>();

        for (String key : keys) {

            if (text.contains(key)) {

                String canonical =
                        NORMALIZE.get(key);

                if (seen.add(canonical)) {
                    found.add(canonical);
                }
            }
        }

        return found;
    }

    // =========================
    // DSA RANKING ENGINE
    // =========================

    static List<Result> diagnose(String input) {

        List<String> symptoms =
                normalizeInput(input);

        Set<String> given =
                new HashSet<>(symptoms);

        PriorityQueue<Result> queue =
                new PriorityQueue<>(
                        (a, b) -> {
                            int scoreCompare =
                                    Double.compare(
                                            b.score,
                                            a.score
                                    );

                            if (scoreCompare != 0)
                                return scoreCompare;

                            return Integer.compare(
                                    b.matched,
                                    a.matched
                            );
                        }
                );

        for (Disease disease : DISEASES) {

            Set<String> matched =
                    new HashSet<>(disease.symptoms);

            matched.retainAll(given);

            if (matched.isEmpty())
                continue;

            /*
             * Score is based on:
             * 1. How many disease symptoms matched
             * 2. How much of the user's recognized input matched
             */

            double diseaseCoverage =
                    (matched.size() * 100.0)
                            / disease.symptoms.size();

            double inputCoverage =
                    (matched.size() * 100.0)
                            / Math.max(1, given.size());

            double score =
                    (diseaseCoverage * 0.65)
                            + (inputCoverage * 0.35);

            score =
                    Math.min(100, score);

            queue.offer(
                    new Result(
                            disease.name,
                            disease.description,
                            matched.size(),
                            disease.symptoms.size(),
                            score,
                            matched
                    )
            );
        }

        List<Result> results =
                new ArrayList<>();

        while (!queue.isEmpty()) {
            results.add(queue.poll());
        }

        return results;
    }

    // =========================
    // JSON
    // =========================

    static String jsonEscape(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ");
    }

    static String toJson(String input) {

        List<String> recognized =
                normalizeInput(input);

        List<Result> results =
                diagnose(input);

        String lower =
                input.toLowerCase();

        boolean emergency =
                lower.matches(
                        ".*\\b(" +
                        "unconscious|" +
                        "fainting|" +
                        "severe chest pain|" +
                        "blue lips|" +
                        "coughing blood|" +
                        "blood in vomit|" +
                        "severe breathing difficulty" +
                        ")\\b.*"
                );

        StringBuilder json =
                new StringBuilder("{");

        // Recognized symptoms
        json.append("\"recognized\":[");

        for (int i = 0;
             i < recognized.size();
             i++) {

            if (i > 0)
                json.append(",");

            json.append("\"")
                    .append(
                            jsonEscape(
                                    recognized.get(i)
                            )
                    )
                    .append("\"");
        }

        json.append("],");

        // Results
        json.append("\"results\":[");

        int limit =
                Math.min(5, results.size());

        for (int i = 0;
             i < limit;
             i++) {

            Result result =
                    results.get(i);

            if (i > 0)
                json.append(",");

            json.append("{");

            json.append("\"disease\":\"")
                    .append(
                            jsonEscape(
                                    result.name
                            )
                    )
                    .append("\",");

            json.append("\"description\":\"")
                    .append(
                            jsonEscape(
                                    result.description
                            )
                    )
                    .append("\",");

            json.append("\"score\":")
                    .append(
                            String.format(
                                    Locale.US,
                                    "%.1f",
                                    result.score
                            )
                    )
                    .append(",");

            json.append("\"matched\":")
                    .append(result.matched)
                    .append(",");

            json.append("\"total\":")
                    .append(result.total)
                    .append(",");

            // Matched symptoms
            json.append("\"matchedSymptoms\":[");

            int j = 0;

            for (String symptom :
                    result.matchedSymptoms) {

                if (j++ > 0)
                    json.append(",");

                json.append("\"")
                        .append(
                                jsonEscape(symptom)
                        )
                        .append("\"");
            }

            json.append("],");

            // Recommended tests
            json.append("\"tests\":[");

            List<String> tests =
                    TESTS.getOrDefault(
                            result.name,
                            Arrays.asList(
                                    "Consult a healthcare professional for appropriate evaluation"
                            )
                    );

            for (int k = 0;
                 k < tests.size();
                 k++) {

                if (k > 0)
                    json.append(",");

                json.append("\"")
                        .append(
                                jsonEscape(
                                        tests.get(k)
                                )
                        )
                        .append("\"");
            }

            json.append("]");

            json.append("}");
        }

        json.append("],");

        json.append("\"emergency\":")
                .append(emergency);

        json.append("}");

        return json.toString();
    }

    // =========================
    // HTTP SERVER
    // =========================

    static void send(
            HttpExchange exchange,
            int code,
            String body,
            String type
    ) throws IOException {

        byte[] bytes =
                body.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        type + "; charset=UTF-8"
                );

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Origin",
                        "*"
                );

        exchange.sendResponseHeaders(
                code,
                bytes.length
        );

        try (
                OutputStream output =
                        exchange.getResponseBody()
        ) {
            output.write(bytes);
        }
    }

    static String readBody(
            InputStream input
    ) throws IOException {

        return new BufferedReader(
                new InputStreamReader(
                        input,
                        StandardCharsets.UTF_8
                )
        )
        .lines()
        .collect(
                Collectors.joining("\n")
        );
    }

    // =========================
    // MAIN
    // =========================

    public static void main(
            String[] args
    ) throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        // Website files
        server.createContext(
                "/",
                exchange -> {

                    String path =
                            exchange
                                    .getRequestURI()
                                    .getPath();

                    if (path.equals("/")
                            || path.equals("/index.html")) {

                        path = "/index.html";
                    }

                    File file =
                            new File("web" + path);

                    if (!file.exists()
                            || file.isDirectory()) {

                        send(
                                exchange,
                                404,
                                "Not found",
                                "text/plain"
                        );

                        return;
                    }

                    String type;

                    if (path.endsWith(".css")) {

                        type = "text/css";

                    } else if (path.endsWith(".js")) {

                        type = "application/javascript";

                    } else {

                        type = "text/html";
                    }

                    send(
                            exchange,
                            200,
                            new String(
                                    java.nio.file.Files
                                            .readAllBytes(
                                                    file.toPath()
                                            ),
                                    StandardCharsets.UTF_8
                            ),
                            type
                    );
                }
        );

        // Diagnosis API
        server.createContext(
                "/api/diagnose",
                exchange -> {

                    if (!exchange
                            .getRequestMethod()
                            .equalsIgnoreCase("POST")) {

                        send(
                                exchange,
                                405,
                                "Method Not Allowed",
                                "text/plain"
                        );

                        return;
                    }

                    String body =
                            readBody(
                                    exchange
                                            .getRequestBody()
                            );

                    String input =
                            body.replaceFirst(
                                    "^\\{\\s*\"symptoms\"\\s*:\\s*\"",
                                    ""
                            )
                            .replaceFirst(
                                    "\"\\s*\\}\\s*$",
                                    ""
                            );

                    input =
                            input.replace(
                                    "\\\"",
                                    "\""
                            );

                    send(
                            exchange,
                            200,
                            toJson(input),
                            "application/json"
                    );
                }
        );

        server.start();

        System.out.println(
                "Symptom-to-Diagnosis Matcher running at http://localhost:8080"
        );
    }
}