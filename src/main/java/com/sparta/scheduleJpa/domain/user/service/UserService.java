package com.sparta.scheduleJpa.domain.user.service;

import com.sparta.scheduleJpa.domain.user.dto.request.UserLoginReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserSignUpReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserUpdateReq;
import com.sparta.scheduleJpa.domain.user.entity.User;
import com.sparta.scheduleJpa.domain.user.exception.AlreadyExistUserException;
import com.sparta.scheduleJpa.domain.user.exception.UserNotFoundException;
import com.sparta.scheduleJpa.domain.user.repository.UserRepository;
import com.sparta.scheduleJpa.global.exception.UnauthorizedException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long signUp(UserSignUpReq request) {
        isExistEmail(request.email());

        User user = userRepository.save(request.toEntity());

        return user.getId();
    }

    public Long login(UserLoginReq request) {
        User user = findUserByEmail(request.email());

        user.isPossibleLogin(request.password());

        return user.getId();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateReq request, Long loginUserId) {
        User user = checkUserAuthentication(userId, loginUserId);

        user.updateName(request.name());
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

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private void isExistEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistUserException(ErrorCode.ALREADY_EXIST_USER);
        }
    }

    public User checkUserAuthentication(Long userId, Long loginUserId) {
        User user = findUserById(userId);

        if (!user.getId().equals(loginUserId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        return user;
    }
}
