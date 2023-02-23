package com.server.dos.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

    private Map<String,Object> attributes;
    private String attributeKey;
    private String email;
    private String nickname;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes){
        switch (provider){
            case "kakao":
                return ofKakao(attributeKey,attributes);
//            case "google":
//                return ofGoogle(attributeKey,attributes);
            default:
                throw new RuntimeException();
        }
    }
    private static OAuth2Attribute ofKakao(String attributeKey, Map<String,Object> attributes){
        Map<String,Object> kakaoAccount = (Map<String,Object>)attributes.get("kakao_account");
        Map<String,Object> kakaoProfile = (Map<String,Object>)kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .nickname((String)kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }
//    private static OAuth2Attribute ofGoogle(String attributeKey, Map<String,Object> attributes){
//
//        return OAuth2Attribute.builder()
//                .nickname((String)googleAccount.get("name"))
//                .email((String) kakaoAccount.get("email"))
//                .attributes(attributes)
//                .attributeKey(attributeKey)
//                .build();
//    }
    public Map<String,Object> convertToMap(){
        Map<String,Object>map = new HashMap<>();
        map.put("id",attributeKey);
        map.put("key",attributeKey);
        map.put("nickname",nickname);
        map.put("email",email);

        return map;
    }
}
