package com.sparta.scheduleJpa.api.user;

import com.sparta.scheduleJpa.domain.user.dto.request.UserSignUpReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserUpdateReq;
import com.sparta.scheduleJpa.domain.user.dto.response.UserDetailRes;
import com.sparta.scheduleJpa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
            @RequestBody final UserSignUpReq request
    ) {
        Long userId = userService.signUp(request);

        URI uri = UriComponentsBuilder.fromPath("/api/users/{userId}")
                .buildAndExpand(userId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailRes> myPage(
            @PathVariable final Long userId
    ) {
        return ResponseEntity.ok(UserDetailRes.from(userService.findUserById(userId)));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
            @PathVariable final Long userId,
            @RequestBody final UserUpdateReq request
    ) {
        userService.updateUser(userId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable final Long userId
    ) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
