package com.sparta.scheduleJpa.domain.user.service;

import com.sparta.scheduleJpa.domain.user.dto.request.UserLoginReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserSignUpReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserUpdateReq;
import com.sparta.scheduleJpa.domain.user.entity.User;
import com.sparta.scheduleJpa.domain.user.exception.AlreadyExistUserException;
import com.sparta.scheduleJpa.domain.user.exception.PasswordNotMatchedException;
import com.sparta.scheduleJpa.domain.user.exception.UserNotFoundException;
import com.sparta.scheduleJpa.domain.user.repository.UserRepository;
import com.sparta.scheduleJpa.global.config.PasswordEncoder;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(UserSignUpReq request) {
        isExistEmail(request.email());

        User user = userRepository.save(request.toEntity(encodePassword(request.password())));

        return user.getId();
    }

    public Long login(UserLoginReq request) {
        User user = findUserByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordNotMatchedException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        return user.getId();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateReq request, Long loginUserId) {
        User user = checkUserAuthentication(userId, loginUserId);

        user.update(request.name(), encodePassword(request.password()));
    }

    @Transactional
    public void deleteUser(Long userId, Long loginUserId) {
        checkUserAuthentication(userId, loginUserId);

        userRepository.deleteById(userId);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public User checkUserAuthentication(Long userId, Long loginUserId) {
        User user = findUserById(userId);

        user.checkLoginUser(loginUserId);

        return user;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private void isExistEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistUserException(ErrorCode.ALREADY_EXIST_USER);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
