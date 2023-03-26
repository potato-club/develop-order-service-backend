package com.server.dos.service;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.TokenDto;
import com.server.dos.dto.UserDto;
import com.server.dos.entity.user.Role;
import com.server.dos.entity.user.User;
import com.server.dos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenForTest {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Transactional
    public TokenDto addDummyUser(UserDto userDto) {
        User findUser = userRepository.findByEmail(userDto.getEmail());
        if(findUser == null) {
            User user = User.builder()
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .picture(null)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }
        TokenDto token = jwtProvider.generateToken(userDto.getEmail(), "USER");
        return token;
    }
}
