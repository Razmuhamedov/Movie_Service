package ms.movie_service.service;

import ms.movie_service.exception.EmailNotDelivered;
import ms.movie_service.util.JwtTokenUtil;
import ms.movie_service.dto.auth.AuthDto;
import ms.movie_service.dto.auth.LoginResultDto;
import ms.movie_service.dto.auth.SignUpDto;
import ms.movie_service.type.Role;
import ms.movie_service.entity.User;
import ms.movie_service.exception.BadRequest;
import ms.movie_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final MailSenderService mailSenderService;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.mailSenderService = mailSenderService;
    }

    public String signUp(SignUpDto dto) {
        Optional<User> optional = userRepository.findByEmailAndDeletedAtIsNull(dto.getEmail());
        if(optional.isPresent()) {
            throw new BadRequest("This email already exist!");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        if(sendMessageToEmail(user)){
            return "Please confirm your email!";
        }
        throw new EmailNotDelivered("Email not delivered!");

    }

    public Boolean sendMessageToEmail(User user) {
        String token = jwtTokenUtil.generateAccessToken(user.getId(), user.getEmail());
        String link = "http://localhost:8080/api/v1/auth/verification/" + token;
        String subject = "Fantastic Movie verification";
        String content = "Click for verification this link: " + link;

        try {
            mailSenderService.send(user.getEmail(), subject, content);
        } catch (Exception e) {
            userRepository.delete(user);
            return false;
        }
        return true;
    }

    public LoginResultDto login(AuthDto dto) {
        Optional<User> optional = userRepository.findByEmailAndDeletedAtIsNull(dto.getEmail());
        User user = optional.get();
        if(optional.isEmpty()){
            throw new BadRequest("User not found!");
        }
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new BadRequest("User not found!");
        }
        String token = jwtTokenUtil.generateAccessToken(user.getId(), user.getEmail());
        return new LoginResultDto(user.getEmail(), token);
    }

    public String verification(String token) {
        if(!jwtTokenUtil.validate(token)){
            throw new BadRequest("Token invalid");
        }
        String email = jwtTokenUtil.getUserName(token);
        Optional<User> optional = userRepository.findByEmailAndDeletedAtIsNull(email);
        if(optional.isEmpty()){
            throw new BadRequest("User not found");
        }
        User user = optional.get();
        if(user.getEmailVerifiedAt() != null){
            throw new BadRequest("User already verified");
        }
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setStatus(true);
        userRepository.save(user);
        return "Successful verified";
     }
}
