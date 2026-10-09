package service;

import com.google.gson.Gson;
import model.AiResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AIService {
    private final String apiKey = System.getenv("GEMINI_API_KEY");

    private HttpResponse<String> response;
    private HttpRequest request;
    private HttpClient client;
    private String aiText = "";
    private AiResponse aiResponse;

    public AiResponse summarize(List<String> chatHistory) {
        // Correct Google Generative AI endpoint format
        var url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key=" + apiKey;

        String chats = String.join("\n", chatHistory);
        //structure
        String prompt = "You are the meeting assistant for ZeroBoard, a collaborative whiteboard application. " +
                        "Analyze the following chat conversation." +
                        " Generate: 1. Brief Summary 2. Key Discussion Points3. Decisions Made 4. Action Items 5. Responsible Person, " +
                        "if mentioned Do not invent information that isn't present in the conversation. CHAT HISTORY :"+chats;

        System.out.println("****THE CHAT HISTRORY IS: "+chats);
//        System.out.println(chatHistory.size());
//        for(String str : chatHistory) {
//            System.out.println(str);
//            prompt += " " + str;
//        }
       String json = """
                      {
                       "contents": [
                        {
                         "parts": [
                           {
                            "text": "%s"
                           }
                          ]
                         }
                       ] 
                      }
                       """.formatted(prompt);

        try {
            System.out.println("DEBUG: Building request to: " + url);
            request = HttpRequest.newBuilder().uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            System.out.println("DEBUG: Sending HTTP request...");
            client = HttpClient.newHttpClient();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG: Response received, status: " + response.statusCode());
            System.out.println("DEBUG: Response body: " + response.body());

            Gson gson = new Gson();
            aiResponse = gson.fromJson(response.body(), AiResponse.class);

        }
        catch(IOException e) {
            System.out.println("ERROR: IOException - " + e.getMessage());
            e.printStackTrace();
            //return "Error: " + e.getMessage();
        }
        catch(InterruptedException e) {
            System.out.println("ERROR: InterruptedException - " + e.getMessage());
            e.printStackTrace();
            Thread.currentThread().interrupt();
            //return "Error: " + e.getMessage();
        }

        if (response == null) {
            System.out.println("ERROR: response is null");
            //return "Could not generate a response try again!!";
        }

        return aiResponse;
    }
}


