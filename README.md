# Spring AI: OpenAI + Ollama

A Spring Boot 4 / Java 21 REST application with one endpoint and two selectable LLM providers:
- `openai`: cloud model (`gpt-4o-mini`)
- `ollama`: local model (`llama3.2:1b`)

## Prerequisites

- JDK 21
- Maven 3.9+
- An OpenAI API key with API billing/credits enabled
- Ollama installed and running

## 1. Download the local Ollama model

Open PowerShell:

```powershell
ollama pull llama3.2:1b
ollama list
```

Ollama normally listens at `http://localhost:11434`.

## 2. Set your OpenAI API key (PowerShell)

For the current PowerShell window:

```powershell
$env:OPENAI_API_KEY = "your-openai-api-key"
```

To persist it for future terminals:

```powershell
[Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "your-openai-api-key", "User")
```

After setting it persistently, restart IntelliJ / the terminal so it receives the updated environment. Do not put the real key in `application.yml` or commit it to Git.

## 3. Run the application

From the project root:

```powershell
mvn clean spring-boot:run
```

The API starts at `http://localhost:8080`.

## 4. Call the single chat endpoint

### OpenAI

```powershell
$body = @{
  model = "openai"
  message = "Explain Spring Boot autoconfiguration in simple terms."
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/chat" -Method Post -ContentType "application/json" -Body $body
```

### Ollama

```powershell
$body = @{
  model = "ollama"
  message = "Explain Spring Boot autoconfiguration in simple terms."
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/chat" -Method Post -ContentType "application/json" -Body $body
```

Example response:

```json
{
  "model": "ollama",
  "response": "Spring Boot autoconfiguration..."
}
```

## Postman

- Method: `POST`
- URL: `http://localhost:8080/api/chat`
- Header: `Content-Type: application/json`
- Body: raw → JSON

```json
{
  "model": "openai",
  "message": "What is Spring AI?"
}
```

Change `model` to `ollama` to use your local model.

## Project structure

```text
src/main/java/com/openai/conceptroom/
├── SpringOpenaiApplication.java
├── config/
│   └── AiModelConfiguration.java
├── controller/
│   └── ChatController.java
├── service/
│   └── ChatService.java
└── dto/
    ├── ChatRequest.java
    └── ChatResponse.java

src/main/resources/
└── application.yml
```

## Notes

Run Ollama locally, either with Docker container or with installing ollama in local.
<img width="1916" height="1010" alt="image" src="https://github.com/user-attachments/assets/1faeea35-dc7b-4e0e-9f81-82ee28e0a6ad" />
Sample Request and Response(Tested with POSTMAN)
<img width="1912" height="1032" alt="image" src="https://github.com/user-attachments/assets/9486b841-c06f-421f-8708-b8d1acac5186" />



- Both model starters are included and both models are explicitly wired to separate `ChatClient` instances.
- `@Primary` selects OpenAI as the default `ChatModel` for Spring AI's auto-configured builder; the service does not rely on that default when routing requests.
- OpenAI calls use the internet and may incur API charges. Ollama calls use the model downloaded on your machine.
- If Ollama is not running, start the Ollama desktop app or run `ollama serve`.
- If Maven cannot resolve dependencies, reload the Maven project in IntelliJ and run `mvn -U clean package`.
