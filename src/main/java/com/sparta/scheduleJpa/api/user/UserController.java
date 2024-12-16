package com.sparta.scheduleJpa.api.user;

import com.sparta.scheduleJpa.domain.user.dto.request.UserLoginReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserSignUpReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserUpdateReq;
import com.sparta.scheduleJpa.domain.user.dto.response.UserDetailRes;
import com.sparta.scheduleJpa.domain.user.service.UserService;
import com.sparta.scheduleJpa.global.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
            @RequestBody @Valid final UserSignUpReq request
    ) {
        final Long userId = userService.signUp(request);

        final URI uri = UriComponentsBuilder.fromPath("/api/users/{userId}")
                .buildAndExpand(userId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final UserLoginReq request,
            HttpServletRequest httpServletRequest
    ) {
        final Long id = userService.login(request);

        SessionUtil.createSession(id, httpServletRequest);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpSession session) {
        SessionUtil.removeSession(session);

        return ResponseEntity.ok().build();
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
            @RequestBody @Valid final UserUpdateReq request,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        userService.updateUser(userId, request, loginUserId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable final Long userId,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        userService.deleteUser(userId, loginUserId);

        return ResponseEntity.noContent().build();
    }
}
