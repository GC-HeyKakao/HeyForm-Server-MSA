package heykakao.HeyForm.service;

import heykakao.HeyForm.model.Choice;
import heykakao.HeyForm.model.Question;
import heykakao.HeyForm.model.Survey;
import heykakao.HeyForm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository, QuestionRepository questionRepository,
                      ChoiceRepository choiceRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
    }

    public void delChoice(Long question_id, Integer choice_order) {
        Choice choice = choiceRepository.findByOrderAndQuestion_Id(choice_order, question_id).get();
        choiceRepository.deleteById(choice.getId());
    }

    public void delQuestion(Long question_id) {
        delQuetionUtil(question_id);
    }

    public void delQuestion(Long survey_id, Integer question_order) {
        Question question = questionRepository.findByOrderAndSurvey_Id(question_order, survey_id).get();
        delQuetionUtil(question.getId());
    }

    private void delQuetionUtil(Long question_id) {
        List<Choice> choices = choiceRepository.findByQuestion_Id(question_id);
        List<Long> choice_ids = choices.stream().map(Choice::getId).collect(Collectors.toList());
        choiceRepository.deleteAllById(choice_ids);

        questionRepository.deleteById(question_id);
    }

    public void delSurvey(Long survey_id) {
        Survey survey = surveyRepository.findById(survey_id).get();

        List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
        List<Long> question_ids = questions.stream().map(Question::getId).collect(Collectors.toList());

        for(Long qs_id: question_ids) {
            delQuestion(qs_id);
        }
        surveyRepository.deleteById(survey.getId());
    }

    public String getUrl(Long survey_id) {
        Survey survey = surveyRepository.findById(survey_id).get();
        return survey.getUrl();
    }

//    public void del

    // 1. User id ????????? ?????? ????????? ?????????????????? (?????? ?????????: question ?????? choice ??? ??????)
//    public List<SurveyInfo> getSurveyInfoById(Long userId) {return new SurveyInfo();}
    // 2. Json ????????? ??????
//    public boolean saveSurveyInfo(SurveyQuestionDto surveyQuestionInfo) {return true;}
//    // 3. ?????? ??????
//    public boolean updateSurveyInfo(SurveyQuestionDto surveyQuestionInfo) {return true;}
//    // 4. ?????? URL
//    public String getReleaseURL(Long surveyKey){return "";}
}

/*
    ?????? : Restful API naming ?????? Swagger? ?????? ???????????? ????????????, ????????? ??????
    ?????? : DI ?????? ?????? ???????????? Service ??????
 */
