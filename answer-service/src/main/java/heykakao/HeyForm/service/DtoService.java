package heykakao.HeyForm.service;

import com.fasterxml.jackson.databind.util.JSONPObject;

import heykakao.HeyForm.model.*;
import heykakao.HeyForm.model.dto.*;
import heykakao.HeyForm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DtoService {
    private final AnswerRepository answerRepository;



    @Autowired
    public DtoService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    // Save

    public void saveAnswer(Long survey_id, Long user_id, List<AnswerDto> answerDtos) {
        for (AnswerDto answerDto : answerDtos) {
            Answer answer = new Answer();
            answer.setByDto(answerDto, user_id,survey_id);
            answerRepository.save(answer);
        }
    }

    public List<AnswerDto> getAnswersBySurveyId(Long survey_id) {
        try {
            return getSurveyAnswerDto(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
    }

    private List<AnswerDto> getSurveyAnswerDto(Long survey_id) {
        List<AnswerDto> answerDtos = new ArrayList<>();

        List<Answer> answers = answerRepository.findBySurvey_id(survey_id);
        for (Answer answer : answers) {
            AnswerDto answerDto = new AnswerDto(answer);
            answerDtos.add(answerDto);
        }
        return answerDtos;
    }

    public List<AnswerDto> getAnswerDtosByUserId(Long user_id){
        List<AnswerDto> answerDtos = new ArrayList<>();

        List<Answer> answers = answerRepository.findByUser_id(user_id);
        for (Answer answer : answers) {
            AnswerDto answerDto = new AnswerDto(answer);
            answerDtos.add(answerDto);
        }
        return answerDtos;
    }



}
