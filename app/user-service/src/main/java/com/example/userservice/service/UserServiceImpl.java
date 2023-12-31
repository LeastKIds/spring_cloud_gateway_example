package com.example.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    // @Autowired
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }


    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity =  userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return userDto;

    }


    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }


    // 이름이 맘에 안 들긴하는데...
    // 이렇게 설정해 놓으면 getUserByEamil이니깐
    // 이메일로 유저를 찾는 기능밖에 못넣을거 같잖아.
    // 일단 여기에 jwt 토큰 생성 기능을 임시방편으로 넣음
    @Override
    public UserEntity getUserByEmail(UserDto userDto, HttpServletResponse response) {
        // 이것의 결과물은 상세한 유저의 정보를 가지고 있음.
        // 이것을 세션에 저장.
        // jwt와 같이 비상태 서버로 운영할 경우
        // 단지 통과 여부만을 확인할 수 있고, 유저의 정보는 가지지 않음
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userDto.getEmail(),
                userDto.getPwd()
                // passwordEncoder.encode(userDto.getPwd())
            )
        );



        return userRepository
            .findByEmail(userDto.getEmail())
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found")
            );
    
    }
}
