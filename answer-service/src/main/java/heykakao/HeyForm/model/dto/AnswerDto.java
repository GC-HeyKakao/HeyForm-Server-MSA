package heykakao.HeyForm.model.dto;

import heykakao.HeyForm.model.Answer;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnswerDto {
    private Long question_id;
    private String answer_contents;
    private String answer_time;

    public AnswerDto(Answer answer) {
        this.question_id= answer.getQuestion_id();
        this.answer_contents = answer.getContents();
        this.answer_time = answer.getTime();
    }
}
