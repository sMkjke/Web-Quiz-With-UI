package engine.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotBlank(message = "Description can't be empty")
    private String text;

    private String author;

//    @NotNull
//    @Size(min = 2)
    @ElementCollection
    private List<String> options;

    protected Quiz() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

//    @Override
//    public String toString() {
//        return "Question{" +
//                "title='" + title + '\'' +
//                ", text='" + text + '\'' +
//                ", options=" + options +
//                ", answer=" + answer +
//                ", id=" + id +
//                '}';
//    }

    //    public boolean isCorrect(List<Integer> options) {
//        return options.stream().sorted().collect(Collectors.toList()).equals(answer);
//    }

    //    @ElementCollection
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private List<Integer> answer;


//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private int id;
}