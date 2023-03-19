package com.server.dos.entity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.Map;


@Slf4j
@Getter
public class OAuth2Attribute {

    private final Map<String,Object>  attributes;
    private final String attributeKey;
    private final String email;
    private final String name;

    private final String picture;

    @Builder
    public OAuth2Attribute(Map<String,Object> attributes,String attributeKey,String email,String name,String picture){
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes){
        switch (provider){
            case "kakao":
                return ofKakao("email",attributes);
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
                .name((String)kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String)kakaoProfile.get("thumbnail_image"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }
    private static OAuth2Attribute ofGoogle(String attributeKey, Map<String,Object> attributes){

        return OAuth2Attribute.builder()
                .name((String)attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
    public Map<String,Object> convertToMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",attributeKey);
        map.put("key",attributeKey);
        map.put("email",email);
        map.put("name",name);
        map.put("picture",picture);
        return map;
    }
}
