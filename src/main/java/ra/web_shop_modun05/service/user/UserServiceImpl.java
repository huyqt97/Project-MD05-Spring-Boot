package ra.web_shop_modun05.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.web_shop_modun05.exception.BlockUser;
import ra.web_shop_modun05.exception.LoginException;
import ra.web_shop_modun05.exception.NotEmptyCustomer;
import ra.web_shop_modun05.exception.RegisterException;
import ra.web_shop_modun05.model.dto.request.UserLoginReq;
import ra.web_shop_modun05.model.dto.request.UserRegisterReq;
import ra.web_shop_modun05.model.dto.request.UserUpdatePasswordReq;
import ra.web_shop_modun05.model.dto.request.UserUpdateReq;
import ra.web_shop_modun05.model.dto.response.UserRes;
import ra.web_shop_modun05.model.entity.Role;
import ra.web_shop_modun05.model.entity.User;
import ra.web_shop_modun05.repository.RoleRepository;
import ra.web_shop_modun05.repository.UserRepository;
import ra.web_shop_modun05.security.jwt.JwtProvider;
import ra.web_shop_modun05.security.user_principal.UserPrincipal;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;
    @Override
    public String register(UserRegisterReq userRegisterReq) throws RegisterException {
        if (!userRepository.existsByUserName(userRegisterReq.getUserName())) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName("USER"));
            User user = User.builder()
                    .userName(userRegisterReq.getUserName())
                    .passWord(passwordEncoder.encode(userRegisterReq.getPassWord()))
                    .fullName(userRegisterReq.getFullName())
                    .email(userRegisterReq.getEmail())
                    .status(true)
                    .roles(roles)
                    .build();
            userRepository.save(user);
            return "Đăng ký thành công!";
        } else {
            throw new RegisterException("Tên tài khoản đã tồn tại!");
        }
    }

    @Override
    public List<UserRes> findAll() {
        List<UserRes> userResList = new ArrayList<>();
        for (User u : userRepository.findAll()) {
            UserRes userRes = new UserRes();
            userRes.setId(u.getId());
            userRes.setUserName(u.getUserName());
            userRes.setEmail(u.getEmail());
            userRes.setFullName(u.getFullName());
            userRes.setStatus(u.getStatus());
            userResList.add(userRes);
        }
        return userResList;
    }

    // chú ý
    @Override
    public UserRes login(UserLoginReq userLoginReq) throws LoginException, BlockUser {
        Authentication authentication;
        try {
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLoginReq.getUserName(), userLoginReq.getPassWord()));
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed with error: " + e.getMessage());
            throw new LoginException("Tài khoản hoặc mật khẩu không đúng!");
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (!userPrincipal.getUser().getStatus()) {
            throw new BlockUser("Tài khoản đã bị khóa!");
        }
        String token = jwtProvider.generateToken(userPrincipal);
        return UserRes.builder()
                .id(userPrincipal.getUser().getId())
                .userName(userPrincipal.getUser().getUserName())
                .email(userPrincipal.getUser().getEmail())
                .status(userPrincipal.getUser().getStatus())
                .roles(userPrincipal.getUser().getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                .fullName(userPrincipal.getUser().getFullName())
                .token(token).build();
    }

    @Override
    public UserRes profile(Authentication authentication) throws NotEmptyCustomer {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            return UserRes.builder()
                    .id(user.getId())
                    .userName(user.getUserName())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .roles(userPrincipal.getUser().getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                    .fullName(user.getFullName())
                    .build();
        }throw new NotEmptyCustomer("Không tìm thấy người dùng!");
    }

    @Override
    public UserRes updateProfile(Authentication authentication, UserUpdateReq userUpdateReq) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user != null) {
            user.setEmail(userUpdateReq.getEmail());
            user.setFullName(userUpdateReq.getFullName());
            User userT = userRepository.save(user);
            return UserRes.builder()
                    .id(userT.getId())
                    .userName(userT.getUserName())
                    .email(userT.getEmail())
                    .status(userT.getStatus())
                    .roles(userPrincipal.getUser().getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                    .fullName(userT.getFullName())
                    .build();
        }throw new NotEmptyCustomer("Không tìm thấy người dùng!");
    }

    @Override
    public String updatePassWord(Authentication authentication, UserUpdatePasswordReq userUpdatePasswordReq) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if(user!= null){
            if(passwordEncoder.matches(userUpdatePasswordReq.getPassWord(), user.getPassWord())){
                user.setPassWord(passwordEncoder.encode(userUpdatePasswordReq.getPassWordNew()));
                userRepository.save(user);
                return "Cập nhập mật khẩu thành công!";
            }else {
                return "Mật khẩu không đúng!";
            }
        }throw new NotEmptyCustomer("Không tìm thấy người dùng!");
    }

    @Override
    public List<UserRes> findAllShow(String search, String filed, String sort, Integer page, Integer limit) throws NotEmptyCustomer {
        Sort sort1 = Sort.by(filed);
        if(sort.equalsIgnoreCase("desc")){
            sort1 = sort1.descending();
        }else {
            sort1 = sort1.ascending();
        }
        Role role = roleRepository.findByRoleName("ADMIN");
        Page<User> users = userRepository.findAllByRole(search,role, PageRequest.of(page,limit).withSort(sort1));
        List<UserRes> userResList = new ArrayList<>();
        for (User u:users) {
            Set<String> set = u.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());

            UserRes res = UserRes.builder()
                    .id(u.getId())
                    .userName(u.getUserName())
                    .fullName(u.getFullName())
                    .email(u.getEmail())
                    .status(u.getStatus())
                    .roles(set).build();
            userResList.add(res);
        }
        return userResList;
    }

    @Override
    public String editStatus(Authentication authentication, Long id) throws NotEmptyCustomer {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        if (user.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
            Optional<User> user1 = userRepository.findById(id);
            if (user1.isPresent()){
                User u = user1.get();
                u.setStatus(!u.getStatus());
                User us = userRepository.save(u);
                if(us.getStatus()){
                    return "đã mở khóa tài khoản!";
                }else {
                    return "đã khóa tài khoản!";
                }
            }throw new NotEmptyCustomer("Không tìm thấy tên ngườ dùng!");
        } else {
            throw new NotEmptyCustomer("Không cấp quyền thao tác!");
        }
    }
}
