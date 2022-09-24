package ms.movie_service.config;

import lombok.Getter;
import lombok.Setter;
import ms.movie_service.entity.User;
import ms.movie_service.type.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private Integer id;
    private String userName;
    private String password;
    private Boolean status;
    private Role role;

    private List<GrantedAuthority> authorityList;

    public CustomUserDetails(User user){
        this.id = user.getId();
        this.userName = user.getName();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.role = user.getRole();

        this.authorityList = List.of(new SimpleGrantedAuthority(role.toString()));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
