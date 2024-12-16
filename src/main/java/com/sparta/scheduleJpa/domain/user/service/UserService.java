package com.sparta.scheduleJpa.domain.user.service;

import com.sparta.scheduleJpa.domain.user.dto.request.UserLoginReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserSignUpReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserUpdateReq;
import com.sparta.scheduleJpa.domain.user.entity.User;
import com.sparta.scheduleJpa.domain.user.exception.UserNotFoundException;
import com.sparta.scheduleJpa.domain.user.repository.UserRepository;
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
        User user = userRepository.save(request.toEntity());

        return user.getId();
    }

    public Long login(UserLoginReq request) {
        User user = findUserByEmail(request.email());

        user.isPossibleLogin(request.password());

        return user.getId();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateReq request) {
        User user = findUserById(userId);

        user.updateName(request.name());
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다"));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다"));
    }
}
