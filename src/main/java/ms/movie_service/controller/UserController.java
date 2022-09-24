package ms.movie_service.controller;

import ms.movie_service.dto.user.CreateUserDto;
import ms.movie_service.dto.user.UserDto;
import ms.movie_service.dto.user.UserFilterDto;
import ms.movie_service.entity.User;
import ms.movie_service.service.UserService;
import ms.movie_service.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/secured/createUser")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto dto){
        UserDto result = userService.createUser(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody CreateUserDto dto){
        UserDto result = userService.updateUser(SecurityUtil.getUserId(), dto);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/filter")
    public ResponseEntity<?> filterUser(@RequestBody @Valid UserFilterDto dto){
        List<UserDto> result = userService.filter(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(){
        UserDto result = userService.getUser(SecurityUtil.getUserId());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam("oldPassword") String oldPassword,
                                            @RequestParam("newPassword") String newPassword){
        String result = userService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok(result);

    }

    // -----------------admin----------------------

    @GetMapping("/secured/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Integer id){
        UserDto result = userService.getUser(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("secured/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id){
        String result = userService.deleteUser(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/secured/getAllUsers")
    public ResponseEntity<?> getAllUsers(@RequestParam("page") Integer page,
                                         @RequestParam("size") Integer size){
        List<UserDto> result = userService.getAllUsers(page, size);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/secured/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id,
                                        @RequestBody @Valid CreateUserDto dto){
        UserDto result = userService.updateUser(id, dto);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/secured/setRole/{id}")
    public ResponseEntity<?> setRoleUser(@PathVariable("id") Integer id,
                                         @RequestParam("role") String role){
        String result = userService.setRole(id, role);
        return ResponseEntity.ok(result);
    }
}
