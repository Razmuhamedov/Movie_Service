package ms.movie_service.config;

import ms.movie_service.entity.User;
import ms.movie_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String userame) throws UsernameNotFoundException {
        System.out.println("Keldi: loadUserByUsername");
        Optional<User> optional =this.userRepository.findByEmailAndDeletedAtIsNull(userame);
        optional.orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        User user =optional.get();
        System.out.println(user);

        return new CustomUserDetails(user);
    }
}
