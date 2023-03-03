package com.server.dos.entity.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

    private Map<String,Object> attributes;
    private String attributeKey;
    private String email;
    private String nickname;

    private String picture;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes){
        switch (provider){
            case "kakao":
                return ofKakao(attributeKey,attributes);
            case "google":
                return ofGoogle(attributeKey,attributes);
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
                .picture((String)kakaoProfile.get("profile_image_url"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }
    private static OAuth2Attribute ofGoogle(String attributeKey, Map<String,Object> attributes){

        return OAuth2Attribute.builder()
                .nickname((String)attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributeKey(attributeKey)
                .build();
    }
    public User toEntity(){
        return User.builder()
                .nickname(nickname)
                .email(email)
                .build();
    }
}
