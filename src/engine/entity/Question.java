package engine.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length(min = 2, max = 200, message = "The question should be between 2 and 150 characters")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ElementCollection
    @CollectionTable(name = "option")
    @MapKeyColumn(name = "number")
    @Column(name = "description")

    private Map<Integer, String> options = new HashMap<>();

    private Integer answerNumber;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<Integer, String> getOptions() {
        return options;
    }

    public void setOptions(Map<Integer, String> options) {
        this.options = options;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Integer getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(Integer answerNumber) {
        this.answerNumber = answerNumber;
    }
}
