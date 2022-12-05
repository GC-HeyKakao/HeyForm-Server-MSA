package heykakao.HeyForm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import heykakao.HeyForm.model.*;
import heykakao.HeyForm.model.dto.*;
import heykakao.HeyForm.repository.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DtoService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    private final QARepository qaRepository;
    JWTService jwtService = new JWTService();

    @Autowired
    public DtoService(SurveyRepository surveyRepository, QuestionRepository questionRepository,
                      ChoiceRepository choiceRepository, QARepository qaRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.qaRepository = qaRepository;
    }

    // Save
//    public Long saveSurvey(String user_token, SurveyQuestionDto surveyQuestionDto) throws Exception {
    public Long saveSurvey(String user_token, SurveyQuestionDto surveyQuestionDto) throws Exception {

//        Object user_account = jwtService.getClaims(jwtService.getClaims(user_token,JWTService.SECRET_KEY),"email");
//
        SurveyDto surveyDto = surveyQuestionDto.getSurveyDto();

        Long user_id = get_UserToken(user_token);



        Survey survey = new Survey();
        survey.setByDto(surveyDto, user_id);

        surveyRepository.save(survey);
        String url = makeUrl(survey.getId());
        survey.setUrl(url);
        surveyRepository.save(survey);
        

        List<QuestionDto> questionDtos = surveyQuestionDto.getQuestionDtos();

        for (QuestionDto questionDto : questionDtos) {
            Question question = new Question();
            question.setByDto(questionDto, survey);
            questionRepository.save(question);

            List<ChoiceDto> choiceDtos = questionDto.getChoiceDtos();

            for (ChoiceDto choiceDto : choiceDtos) {
                Choice choice = new Choice();
                choice.setByDto(choiceDto, question);
                choiceRepository.save(choice);
            }
        }

        return survey.getId();
    }

    public void saveQa(QADto qaDto){
        QA qa = new QA();
        qa.setByDto(qaDto);
        qaRepository.save(qa);
    }
    public void saveQaAnswer(QADto qaDto){
        QA qa = qaRepository.findById(qaDto.getQa_id()).get();
        qa.setByDto(qaDto);
        qaRepository.save(qa);
    }
    public void delQa(Long qa_id){
        qaRepository.deleteById(qa_id);
    }

    //error x
    private String makeUrl(Long survey_id) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(String.valueOf(survey_id).getBytes());
        timecheck(survey_id);
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }

        return hexText;
    }




    // Update
    //error x
    public void updateSurveyInfo(SurveyDto surveyDto) {

        try {
            Survey survey = surveyRepository.findById(surveyDto.getSurvey_id()).get();
            survey.setByDto(surveyDto);
            surveyRepository.save(survey);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다");
        }

    }

    public void updateQuestion(Long survey_id, QuestionDto questionDto) {
        try {
            surveyRepository.findById(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
        try {
            Question question = questionRepository.findByOrderAndSurvey_Id(questionDto.getQuestion_order(), survey_id).get();
            question.setByDto(questionDto);
            questionRepository.save(question);
        } catch (Exception e) {
            throw new IllegalStateException("해당 질문이 존재하지 않습니다.");
        }
    }

    public void updateChoice(Long question_id, ChoiceDto choiceDto) {
        try {
            Choice choice = choiceRepository.findByOrderAndQuestion_Id(choiceDto.getChoice_order(), question_id).get();
            choice.setByDto(choiceDto);
            choiceRepository.save(choice);
        } catch (Exception e) {
            throw new IllegalStateException("해당 choice가 존재하지 않습니다.");
        }
    }

    //error x
    public void updateAllChoices(Long question_id, List<ChoiceDto> choiceDtos) {
        try {
            questionRepository.findById(question_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 질문이 존재하지 않습니다.");
        }
        try {
            List<Choice> choices = choiceRepository.findByQuestion_Id(question_id);
            for (Choice choice : choices) {
                ChoiceDto choiceDto = choiceDtos.stream().filter(ch_dto -> ch_dto.getChoice_order().equals(choice.getOrder())).collect(Collectors.toList()).get(0);
                choice.setByDto(choiceDto);
                choiceRepository.save(choice);
            }
        } catch (Exception e) {
            throw new IllegalStateException("해당 choice가 존재하지 않습니다.");
        }
    }

    //error x
    public void updateAllQuestions(Long survey_id, List<QuestionDto> questionDtos) {
        try {
            surveyRepository.findById(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
        try {
            List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
            for (Question question : questions) {
                QuestionDto questionDto = questionDtos.stream().filter(qs_dto -> qs_dto.getQuestion_order().equals(question.getOrder())).collect(Collectors.toList()).get(0);
                question.setByDto(questionDto);
                questionRepository.save(question);
                updateAllChoices(question.getId(), questionDto.getChoiceDtos());
            }
        } catch (Exception e) {
            throw new IllegalStateException("해당 질문이 존재하지 않습니다,");
        }
    }

    //error x
    public String updateSurvey(SurveyQuestionDto surveyQuestionDto) throws NoSuchAlgorithmException {

        SurveyDto surveyDto = surveyQuestionDto.getSurveyDto();
        List<QuestionDto> questionDtos = surveyQuestionDto.getQuestionDtos();
        updateSurveyInfo(surveyDto);
        updateAllQuestions(surveyDto.getSurvey_id(), questionDtos);
        Survey survey = surveyRepository.getById(surveyDto.getSurvey_id());
        survey.setUrl(makeUrl(surveyDto.getSurvey_id()));
        surveyRepository.save(survey);
        return survey.getUrl();
    }



    // Get
    public List<SurveyQuestionDto> getSurveysByUserToken(String user_token) {
        try {
//            Object user_account = jwtService.getClaims(jwtService.getClaims(user_token,JWTService.SECRET_KEY),"email");
            Long user_id = get_UserToken(user_token);
            return getSurveyQuestionDtos(user_id);
        } catch (Exception e) {
            throw new IllegalStateException("일치 정보가 없습니다.");
        }
    }


    public SurveyQuestionDto getSurveyQuestionBySurveyId(Long survey_id) {

        try {
            return getSurveyQuestionDto(survey_id);
        } catch (Exception e) {
            throw new RuntimeException("없는 설문조사입니다.");
        }
    }

    public SurveyQuestionDto getSurveyQuestionByUrl(String survey_url) {
        try {
            Survey survey = surveyRepository.findByUrl(survey_url).get();
            return survey2surveyQuestionDto(survey);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
    }



    private SurveyQuestionDto survey2surveyQuestionDto(Survey survey) {
        try {
            SurveyDto surveyDto = new SurveyDto(survey);
            List<QuestionDto> questionDtos = new ArrayList<>();
            List<Question> questions = questionRepository.findBySurvey_Id(survey.getId());

            for (Question question : questions) {
                questionDtos.add(question2questionDto(question));
            }

            return new SurveyQuestionDto(surveyDto, questionDtos);
        } catch (Exception e) {
            throw new IllegalStateException("오류");
        }
    }

    private QuestionDto question2questionDto(Question question) {
        try {
            List<Choice> choices = choiceRepository.findByQuestion_Id(question.getId());
            List<ChoiceDto> choiceDtos = new ArrayList<>();
            for (Choice choice : choices) {
                ChoiceDto choiceDto = new ChoiceDto(choice);
                choiceDtos.add(choiceDto);
            }
            return new QuestionDto(question, choiceDtos);
        } catch (Exception e) {
            throw new IllegalStateException("오류");
        }
    }

    private List<ChoiceDto> getChoiceDtos(Long question_id) {
        List<ChoiceDto> choiceDtos = new ArrayList<>();
        List<Choice> choices = choiceRepository.findByQuestion_Id(question_id);
        for (Choice choice : choices) {
            ChoiceDto choiceDto = new ChoiceDto(choice);
            choiceDtos.add(choiceDto);
        }
        return choiceDtos;
    }

    private List<QuestionDto> getQuestionDtos(Long survey_id) {
        List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
        List<QuestionDto> questionDtos = new ArrayList<>();
        for (Question question : questions) {
            List<ChoiceDto> choiceDtos = getChoiceDtos(question.getId());

            QuestionDto questionDto = new QuestionDto(question, choiceDtos);
            questionDtos.add(questionDto);
        }
        return questionDtos;
    }

    //여기
    private SurveyQuestionDto getSurveyQuestionDto(Long survey_id) {
        Survey survey = surveyRepository.findById(survey_id).get();
        SurveyDto surveyDto = new SurveyDto(survey);
        timecheck(surveyDto.getSurvey_id());
        List<QuestionDto> questionDtos = getQuestionDtos(survey_id);

        return new SurveyQuestionDto(surveyDto, questionDtos);
    }

    //여기
    private List<SurveyQuestionDto> getSurveyQuestionDtos(Long user_id) {
        List<SurveyQuestionDto> surveyQuestionDtos = new ArrayList<>();
        List<Long> survey_ids = surveyRepository.findByUser_id(user_id).stream().map(Survey::getId).collect(Collectors.toList());
        for (Long survey_id : survey_ids) {
            timecheck(survey_id);
            surveyQuestionDtos.add(getSurveyQuestionDto(survey_id));
        }
        return surveyQuestionDtos;
    }



    private void timecheck(Long survey_id){
        Survey survey = surveyRepository.getById(survey_id);
        Timestamp start_time = Timestamp.valueOf(survey.getStarttime());
        Timestamp end_time = Timestamp.valueOf(survey.getEndtime());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(end_time.before(now)){
            survey.setState(2);
        }
        else if (start_time.after(now)){
            survey.setState(0);
        }
    }
    public boolean Urlcheck(String url){
        if (surveyRepository.findByUrl(url).isPresent()){return true;}
        else{return false;}
    }




    public boolean tokencheck(String token){

        if (!jwtService.validateToken(token)){
            return false;
        }
        else{
            return true;
        }
    }



    public Long get_UserToken(String target) throws Exception {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(target);

        try{
            HttpClient client = new DefaultHttpClient();
            MyHttpGetWithEntity e = new MyHttpGetWithEntity("http://172.16.4.35:8000/user/token/service/"+target);
            HttpResponse response = client.execute(e);
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                result = handler.handleResponse(response);
            }
            System.out.println(result);

        }catch (Exception e){System.err.println(e);}

        return Long.parseLong(result);
    }

}
