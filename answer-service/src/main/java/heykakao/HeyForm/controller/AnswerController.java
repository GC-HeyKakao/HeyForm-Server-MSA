package heykakao.HeyForm.controller;
import heykakao.HeyForm.exception.ResourceNotFoundException;
import heykakao.HeyForm.model.Answer;
import heykakao.HeyForm.model.dto.AnswerDto;
import heykakao.HeyForm.repository.AnswerRepository;
import heykakao.HeyForm.service.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@ResponseBody
public class AnswerController {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    DtoService dtoService;
    @GetMapping("/answer")
    public List<Answer> getAllAnswer(){
        return answerRepository.findAll();
    }

    @GetMapping("/answer/survey/{surveyId}")
//    @ApiOperation(value = "설문조사 답변 조회", notes = "설문조사 id로 모든 답변 조회")
    public List<AnswerDto> getAnswersBySurveyId(@PathVariable Long surveyId){
        return dtoService.getAnswersBySurveyId(surveyId);
    }

    @GetMapping("/answer/user/{userId}")
//    @ApiOperation(value = "설문조사 유저 답변 조회", notes = "user_id로 모든 답변 조회")
    public List<AnswerDto> getAnswersByUserId(@PathVariable Long userId){
        return  dtoService.getAnswerDtosByUserId(userId);
    }

    @PostMapping("/answer/{userId}")
    public void saveAnswerByUserId(@RequestParam Long surveyId, @PathVariable Long userId, @RequestBody List<AnswerDto> answerDtos){
        dtoService.saveAnswer(surveyId,userId,answerDtos);
    }

    @GetMapping("/answer/survey/result/{surveyId}")
//    @ApiOperation(value= "설문조사 분석 결과")
    public String getTotalAnswer(@PathVariable Long surveyId) throws Exception {
        return dtoService.statistic(surveyId);
    }

}
