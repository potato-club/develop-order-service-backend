package com.server.dos.mapper;

import com.server.dos.dto.UserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserOauthMapper {
    public UserDto getUser(OAuth2User oAuth2User){
        var attributes = oAuth2User.getAttributes();
        return UserDto.builder()
                .nickname((String) attributes.get("nickname"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .build();
    }
}
