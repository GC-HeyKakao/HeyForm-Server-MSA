package heykakao.HeyForm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import heykakao.HeyForm.model.*;
import heykakao.HeyForm.model.dto.*;
import heykakao.HeyForm.repository.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;

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

    public String statistic(Long survey_id) throws Exception {
        JSONObject result = new JSONObject();
        LinkedHashMap<Long, String> question_lists = new LinkedHashMap<>();
        LinkedHashMap<Long, String[]> user_lists = new LinkedHashMap<>();
        Integer sequence = 0;
        List<Answer> answers = answerRepository.findBySurvey_id(survey_id);
//        List<Long> question_ids = new ArrayList<>();
//        List<Long> user_ids = new ArrayList<>();

        for (Answer answer : answers) {
//            Long question_id = answer.getQuestion_id();
//            if(!question_ids.contains(question_id)){
//                question_ids.add(question_id);
//            }
//            Long user_id = answer.getUser_id();
//            if(!user_ids.contains(user_id)){
//                user_ids.add(user_id);
//            }
            Long question_id = answer.getQuestion_id();
            if (question_lists.get(question_id) == null) {
                question_lists.put(question_id,get_QuestionId(String.valueOf(question_id)));
            }
            Long user_id = answer.getUser_id();
            if (user_lists.get(user_id) == null){
                String[] tmp = get_UserInfo(String.valueOf(user_id)).split(" ");
                user_lists.put(user_id, tmp);
            }
        }

        for(Long question_id : question_lists.keySet()){
            JSONArray titleJson = new JSONArray();
            JSONArray genderJson = new JSONArray();
            JSONArray ageJson = new JSONArray();


            LinkedHashMap<String,Integer> titles = new LinkedHashMap<>();
            LinkedHashMap<String,Integer> genders = new LinkedHashMap<>();
            LinkedHashMap<String,Integer> ages = new LinkedHashMap<>();

            if (question_lists.get(question_id).equals("주관식")){
                return "주관식";
            }

            else {
                answers = answerRepository.findByQuestion_id(question_id);
                for (Answer answer : answers) {

                    String content = answer.getContents();
                    if (!titles.containsKey(content)) {
                        titles.put(content, 1);
                    } else {
                        titles.replace(content, titles.get(content) + 1);
                    }


                    String gender = user_lists.get(answer.getUser_id())[1];
                    if (!genders.containsKey(gender)){
                        genders.put(gender,1);
                    }
                    else{
                        genders.replace(gender, genders.get(gender) + 1);
                    }

                    String age = user_lists.get(answer.getUser_id())[0];
                    if(!ages.containsKey(age)){
                        ages.put(age,1);
                    }
                    else{
                        ages.replace(age,ages.get(age)+1);
                    }
                }

                for (String title : titles.keySet()) {
                    JSONObject jsonObject_title = new JSONObject();
                    if (question_lists.get(question_id).equals("단답식"))
                    {
                        jsonObject_title.put("value",title);
                        jsonObject_title.put("count",titles.get(title));
                    }
                    else if(question_lists.get(question_id).equals("객관식")){
                        jsonObject_title.put("choice",title);
                        jsonObject_title.put("응답수",titles.get(title));

                    }
                    else if(question_lists.get(question_id).equals("리커트")){
                        jsonObject_title.put("likert",title);
                        jsonObject_title.put("응답수",titles.get(title));
                    }
                    else if(question_lists.get(question_id).equals("별점")){
                        jsonObject_title.put("star",title);
                        jsonObject_title.put("응답수",titles.get(title));
                    }
                    else if(question_lists.get(question_id).equals("감정바")){
                        jsonObject_title.put("id",title);
                        jsonObject_title.put("value",titles.get(title));
                    }
                    titleJson.add(jsonObject_title);
                }

                for (String gender : genders.keySet()){
                    JSONObject jsonObject_gender = new JSONObject();
                    jsonObject_gender.put("gender",gender);
                    jsonObject_gender.put("응답자수",genders.get(gender));
                    genderJson.add(jsonObject_gender);
                }

                for (String age : ages.keySet()){
                    JSONObject jsonObject_age = new JSONObject();
                    jsonObject_age.put("id",age);
                    jsonObject_age.put("value",ages.get(age));
                    ageJson.add(jsonObject_age);
                }
            }

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(titleJson);
            jsonArray.add(genderJson);
            jsonArray.add(ageJson);
            sequence = sequence + 1;
            result.put(sequence,jsonArray);
        }
        Gson gson = new Gson();

        return gson.toJson(result);
    }

    public String get_QuestionId(String target) throws Exception {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(target);

        try{
            HttpClient client = new DefaultHttpClient();
            MyHttpGetWithEntity e = new MyHttpGetWithEntity("http://localhost:8082/question/"+target);
            HttpResponse response = client.execute(e);
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                result = handler.handleResponse(response);
            }
            System.out.println(result);

        }catch (Exception e){System.err.println(e);}

        return result;
    }
    //return user.getAge() + " " + user.getGender();
    public String get_UserInfo(String target) throws Exception {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(target);

        try{
            HttpClient client = new DefaultHttpClient();
            MyHttpGetWithEntity e = new MyHttpGetWithEntity("http://localhost:8082/user/info/"+target);
            HttpResponse response = client.execute(e);
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                result = handler.handleResponse(response);
            }
            System.out.println(result);

        }catch (Exception e){System.err.println(e);}

        return result;
    }

}
