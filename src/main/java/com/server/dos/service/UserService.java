package com.server.dos.service;

import com.server.dos.entity.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    @Transactional
    public boolean deleteKakaoUser(User user){
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        String token = user.getToken();
        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            // 탈퇴 시 반환되는 id 값
            while ((line = br.readLine()) != null){
                result += line;
            }
            log.info(result);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteGoogleUser(User user) {
        String reqURL = "https://accounts.google.com/o/oauth2/revoke";
        String token = user.getToken();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String postParameters = "token=" + token;

            // Set the request body
            conn.setDoOutput(true);
            try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                out.writeBytes(postParameters);
                out.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                // Handle the response appropriately (e.g., log or throw an exception)
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
