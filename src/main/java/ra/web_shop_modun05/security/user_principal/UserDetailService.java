package ra.web_shop_modun05.security.user_principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.model.entity.User;
import ra.web_shop_modun05.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUsers = userRepository.findByUserName(username);
        UserPrincipal userPrincipal;
        if (optionalUsers.isPresent()) {
            User users = optionalUsers.get();
            userPrincipal = UserPrincipal.builder()
                    .user(users)
                    .authorities(users.getRoles().stream().map(item -> new SimpleGrantedAuthority(item.getRoleName())).collect(Collectors.toSet()))
                    .build();
            return userPrincipal;
        } else {
            throw new UsernameNotFoundException(username + " not found");
        }
    }
}
