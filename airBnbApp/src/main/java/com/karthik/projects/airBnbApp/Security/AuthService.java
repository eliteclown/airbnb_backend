package com.karthik.projects.airBnbApp.Security;

import com.karthik.projects.airBnbApp.dtos.LoginDTO;
import com.karthik.projects.airBnbApp.dtos.SignUpDTO;
import com.karthik.projects.airBnbApp.dtos.UserDTO;
import com.karthik.projects.airBnbApp.entities.User;
import com.karthik.projects.airBnbApp.entities.enums.Role;
import com.karthik.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.karthik.projects.airBnbApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public UserDTO signUp(SignUpDTO signUpDTO){
        User user = userRepository.findByEmail(signUpDTO.getEmail()).orElse(null);

        if(user!=null){
            throw  new BadCredentialsException("User is already present with the same email id");
        }

        User newUser = modelMapper.map(signUpDTO,User.class);
        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        newUser=userRepository.save(newUser);

        return modelMapper.map(newUser,UserDTO.class);
    }

    public String[] login(LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate((
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())
                ));

        User user =  (User)authentication.getPrincipal();

        String[] arr = new String[2];
        arr[0]=jwtService.generateAccessToken(user);
        arr[1]=jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken){
        Long id = jwtService.getUserIdByToken(refreshToken);

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+id));

        return jwtService.generateAccessToken(user);
    }

}
