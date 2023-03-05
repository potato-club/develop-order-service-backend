package com.server.dos.service;

import com.server.dos.entity.user.OAuth2Attribute;
import com.server.dos.entity.user.User;
import com.server.dos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;


    //사용자 정보를 요청할 수 있는 access token을 얻고나서 실행
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // accessToken을 이용해 생성된 Service 객체로부터 User(사용자 정보) 받기
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분하는 코드(어떤 소셜로 로그인인지 구분)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 Key가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId: " + registrationId);
        log.info("userNameAttributeName: " + userNameAttributeName);

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());
        User user = saveOrUpdate(oAuth2Attribute);

//        var memberAttributes = oAuth2Attribute.convertToMap();
        // memberAttribute: {nickname=카카오 이름, id=id, key=id, email=카카오 이메일}

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                oAuth2Attribute.getAttributes(),oAuth2Attribute.getAttributeKey());
    }

    public User saveOrUpdate(OAuth2Attribute oAuth2Attribute){
        User user = userRepository.findByEmail(oAuth2Attribute.getEmail())
                .map(entity->entity.update(oAuth2Attribute.getName(),oAuth2Attribute.getPicture()))
                .orElse(oAuth2Attribute.toEntity());

        return userRepository.save(user);
    }
}
