package engine.controller;


public class QuizResult {

    public final static QuizResult CORRECT_ANSWER = new QuizResult(true, "Congratulations, you're right!");
    public final static QuizResult WRONG_ANSWER = new QuizResult(false, "Wrong answer! Please, try again.");

    private boolean success;
    private String feedback;

    public QuizResult(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
