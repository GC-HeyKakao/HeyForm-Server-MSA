package heykakao.HeyForm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import heykakao.HeyForm.model.dto.AnswerDto;
import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
@ToString(exclude = "question")
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Answer_id")
    private Long id;

    @Column(name = "question_order")
    private Integer order;

    @Column(name = "answer_contents")
    private String contents;

    @Column(name = "answer_time")
    private String time;

    @Column(name = "survey_id")
    private Long survey_id;

    @Column(name = "question_id")
    private Long question_id;

    @Column(name = "user_id")
    private Long user_id;

    public Answer(Integer order, String contents) {
        this.order = order;
        this.contents = contents;
    }

    public void setByDto(AnswerDto answerDto, Long user_id, Long survey_id) {
        this.question_id= answerDto.getQuestion_id();
        this.contents = answerDto.getAnswer_contents();
        this.user_id = user_id;
        this.question_id = answerDto.getQuestion_id();
        this.time = answerDto.getAnswer_time();
        this.survey_id = survey_id;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }
//    public void setQuestion(Question question) {
//        this.question = question;
//    }
}
