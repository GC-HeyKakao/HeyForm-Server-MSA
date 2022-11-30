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
    private final UserRepository userRepository;


    @Autowired
    public DtoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getIdByEmail(String user_email) {
        Optional<User> user = userRepository.findByEmail(user_email);
        return user.get().getId();
    }

    public Long getUserIdByToken(String user_token){
//        Object user_account = jwtService.getClaims(jwtService.getClaims(user_token,JWTService.SECRET_KEY),"email");
        return userRepository.findByAccount(user_token).get().getId();
    }

    public String getUserInfoByUserId(Long user_id){
        User user = userRepository.getReferenceById(user_id);
        return user.getAge() + " " + user.getGender();
    }

    public User findUserByAccount(String Account){
        return userRepository.findByAccount(Account).get();
    }

    public User deleteJWTToken(String kakaoToken){
        User user = userRepository.findByAccount(kakaoToken).get();
        String jwtToken = user.getToken();
        user.setToken("");
        userRepository.save(user);
        user.setToken(jwtToken);
        return user;
    }
}
