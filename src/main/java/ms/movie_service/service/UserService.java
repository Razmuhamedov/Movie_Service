package ms.movie_service.service;

import ms.movie_service.dto.user.CreateUserDto;
import ms.movie_service.dto.user.UserDto;
import ms.movie_service.dto.user.UserFilterDto;
import ms.movie_service.type.Role;
import ms.movie_service.entity.User;
import ms.movie_service.exception.BadRequest;
import ms.movie_service.exception.EmailNotDelivered;
import ms.movie_service.repository.UserRepository;
import ms.movie_service.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(CreateUserDto dto){
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return convertToDto(user, new UserDto());
    }

    public UserDto getUser(Integer userId){
        return convertToDto(getEntity(userId), new UserDto());
    }

    public UserDto updateUser(Integer userId, CreateUserDto dto){
        User user = getEntity(userId);
        if(dto.getEmail() != null){
            if(!authService.sendMessageToEmail(user)){
                throw new EmailNotDelivered("Email not delivered!");
            }
            user.setEmail(dto.getEmail());
        }
        if(dto.getAge() != null){
            user.setAge(dto.getAge());}
        if(dto.getName() != null){
            user.setName(dto.getName());}
        user.setUpdatedAt(LocalDateTime.now());
        user.setEmailVerifiedAt(null);
        user.setStatus(false);
        userRepository.save(user);
        return convertToDto(user, new UserDto());
    }

    //todo: create softDelete

    public String deleteUser(Integer userId){
        User user = getEntity(userId);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.delete(user);
        return "User deleted!";
    }

    public User getEntity(Integer userId){
        Optional<User> optional = userRepository.findById(userId);
        if(optional.isEmpty()){
            throw new BadRequest("User not found!");
        }
        return optional.get();
    }

    public UserDto convertToDto(User user, UserDto dto){
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        return dto;
    }


    public List<UserDto> filter(UserFilterDto dto) {
        String sortBy = "createdAt";
        if(dto.getSortBy()!=null){
            sortBy= dto.getSortBy();
        }
        PageRequest pageRequest= PageRequest.of(dto.getPage(), dto.getSize(), dto.getDirection(), sortBy);
        List<Predicate> predicates = new ArrayList<>();
        Specification<User> specification = ((root, query, criteriaBuilder) -> {
            if(dto.getName() != null){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + dto.getName() + "%"));
            }
            if(dto.getEmail() != null){
                predicates.add(criteriaBuilder.equal(root.get("email"), dto.getEmail()));
            }
            if (dto.getMinAge() != null && dto.getMaxAge() != null) {
                predicates.add((criteriaBuilder.between(root.get("age"), dto.getMinAge(), dto.getMaxAge())));
            }
            if(dto.getMinAge() != null && dto.getMaxAge() == null){
                predicates.add(criteriaBuilder.greaterThan(root.get("age"), dto.getMinAge()));
            }
            if(dto.getMinAge() == null && dto.getMaxAge() != null){
                predicates.add(criteriaBuilder.lessThan(root.get("age"), dto.getMaxAge()));
            }
            if(dto.getMinCreatedDate() != null && dto.getMaxCreatedDate() != null){
                predicates.add(criteriaBuilder.between(root.get("createdAt"), dto.getMinCreatedDate(), dto.getMaxCreatedDate()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        Page<User> page = userRepository.findAll(specification,pageRequest);

        return  page.stream().map(user -> convertToDto(user, new UserDto())).collect(Collectors.toList());
    }

    public List<UserDto> getAllUsers(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> pages = userRepository.findAll(pageRequest);
        return pages.stream().map(user -> (convertToDto(user, new UserDto()))).collect(Collectors.toList());
    }


    public String setRole(Integer id, String role) {
        User user = getEntity(id);
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
        return "Role updated!";
    }

    public String changePassword(String oldPassword, String newPassword) {
        User user = getEntity(SecurityUtil.getUserId());
        if(!passwordEncoder.matches(user.getPassword(), oldPassword)){
            throw new BadRequest("Password is incorrect!");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password updated!";
    }
}
