package model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AiResponse implements Serializable {
    @SerializedName("candidates")
    private List<Candidate> candidates;

    public List<Candidate> getCandidates() { return candidates; }

    public static class Candidate implements Serializable {
        @SerializedName("content") // Singular!
        private Content content;

        public Content getContent() { return content; }
    }

    public static class Content implements Serializable {
        @SerializedName("parts")
        private List<Part> parts;

        public List<Part> getParts() { return parts; }
    }

    public static class Part implements Serializable {
        @SerializedName("text")
        private String text;

        public String getText() { return text; }
    }
}

