package heykakao.HeyForm.controller;

import heykakao.HeyForm.model.Question;
import heykakao.HeyForm.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@ResponseBody
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/question")
    public List<Question> getAllQuestion(){
        return questionRepository.findAll();
    }

    @GetMapping("/question/{questionId}")
    public String getQuestionType(@PathVariable Long questionId){
        return questionRepository.getReferenceById(questionId).getType();
    }
}
