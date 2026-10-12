const input = document.getElementById("symptoms");
const check = document.getElementById("checkBtn");
const output = document.getElementById("output");
const loading = document.getElementById("loading");
const results = document.getElementById("results");
const recognized = document.getElementById("recognized");
const emergency = document.getElementById("emergency");
const notRecognized = document.getElementById("notRecognized");

document.querySelectorAll(".chips button").forEach(b => {
  b.onclick = () => {
    input.value = b.dataset.text;
  };
});

document.getElementById("clearBtn").onclick = () => {
  input.value = "";
  output.classList.add("hidden");
  results.innerHTML = "";
};

check.onclick = async () => {

  const text = input.value.trim();

  if (!text) {
    alert("Please enter at least one symptom.");
    return;
  }

  loading.classList.remove("hidden");
  output.classList.add("hidden");
  check.disabled = true;

  try {

    const res = await fetch("/api/diagnose", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        symptoms: text
      })
    });

    const data = await res.json();

    render(data);

  } catch (e) {

    alert(
      "Could not connect to the Java server. Make sure Main.java is running."
    );

  } finally {

    loading.classList.add("hidden");
    check.disabled = false;
  }
};

function render(data) {

  output.classList.remove("hidden");

  emergency.classList.toggle(
    "hidden",
    !data.emergency
  );

  recognized.innerHTML =
    data.recognized
      .map(x => `<span class="pill">${pretty(x)}</span>`)
      .join("");

  notRecognized.textContent =
    data.recognized.length
      ? "Only symptoms that can be mapped to the project's medical knowledge base are used for scoring."
      : "No known symptom was recognized. Try describing the symptom more specifically.";

  if (!data.results.length) {

    results.innerHTML = `
      <div class="card result">
        <h3>Insufficient matching information</h3>
        <p>
          The entered text did not match enough symptoms
          in the current knowledge base.
          Try entering more specific symptoms.
        </p>
      </div>
    `;

    return;
  }

  results.innerHTML = data.results.map((r, i) => {

    const tests = r.tests || [];

    return `
      <div class="card result">

        <div class="result-top">

          <div>
            <h3>${i + 1}. ${r.disease}</h3>

            <p>${r.description}</p>
          </div>

          <div class="score">

            <b>${r.score}%</b>

            <div class="bar">
              <div
                class="fill"
                style="width:${r.score}%">
              </div>
            </div>

          </div>

        </div>

        <div class="matched">
          Matched symptoms:
          <span>
            ${r.matchedSymptoms.map(pretty).join(", ")}
          </span>
        </div>

        <div class="tests">

          <h4>Recommended Diagnostic Tests</h4>

          <ul>
            ${
              tests.length
                ? tests.map(test => `
                    <li>${test}</li>
                  `).join("")
                : `
                    <li>
                      Consult a healthcare professional
                      for appropriate evaluation.
                    </li>
                  `
            }
          </ul>

        </div>

      </div>
    `;

  }).join("");
}

function pretty(s) {

  return s.replace(
    /\b\w/g,
    c => c.toUpperCase()
  );
}