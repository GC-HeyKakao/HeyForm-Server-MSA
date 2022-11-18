package heykakao.HeyForm.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
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

    JWTService jwtService = new JWTService();

    @Autowired
    public DtoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getTokenByEmail(String user_email) {
        Optional<User> user = userRepository.findByEmail(user_email);
        String jwtToken = jwtService.createToken(JWTService.SECRET_KEY, user.get().getAccount());
        return jwtToken;
    }


    public Long getIdByEmail(String user_email) {
        Optional<User> user = userRepository.findByEmail(user_email);
        return user.get().getId();
    }


    public boolean tokencheck(String token){

        if (!jwtService.validateToken(token)){
            return false;
        }
        else{
            return true;
        }
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
