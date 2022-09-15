package ms.movie_service.config;

import ms.movie_service.entity.User;
import ms.movie_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsSerivce implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsSerivce(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("Keldi: loadUserByUsername");
        Optional<User> optional =userRepository.findByName(userName);
        optional.orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        User user =optional.get();
        System.out.println(user);

        return new CustomUserDetails(user);
    }
}
