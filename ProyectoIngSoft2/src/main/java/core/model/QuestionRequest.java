package core.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionRequest {
    private final String title;
    private final String content;
    private final String type;
    private final List<String> options;
    private final int correctAnswer;

    public QuestionRequest(String title, String content, String type) {
        this(title, content, type, List.of(), 0);
    }

    public QuestionRequest(String title, String content, String type,
                           List<String> options, int correctAnswer) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.options = options == null ? new ArrayList<>() : new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getType() { return type; }
    public List<String> getOptions() { return new ArrayList<>(options); }
    public int getCorrectAnswer() { return correctAnswer; }
}
