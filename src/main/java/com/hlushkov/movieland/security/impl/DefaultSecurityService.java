package com.hlushkov.movieland.security.impl;

import com.hlushkov.movieland.common.request.AuthRequest;
import com.hlushkov.movieland.dao.UserDao;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.entity.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultSecurityService implements SecurityService {
    private final UserDao userDao;
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    @Value("${session.max.age}")
    private long sessionMaxAge;

    @Override
    public Optional<Session> login(AuthRequest authRequest) {

        User user = userDao.findByEmail(authRequest.getEmail());

        if (checkPassword(user, authRequest.getPassword())) {
            String randomUUID = UUID.randomUUID().toString();

            Session session = Session.builder()
                    .user(user)
                    .userUUID(randomUUID)
                    .expireDate(LocalDateTime.now().plusSeconds(sessionMaxAge))
                    .build();

            log.debug("Adding new session : {}", session);
            sessionMap.put(randomUUID, session);
            return Optional.of(session);
        }
        return Optional.empty();
    }

    @Override
    public boolean removeSession(String userUUID) {
        return sessionMap.remove(userUUID) != null;
    }

    @Override
    public Optional<User> getUserByUUID(String userUUID) {
        if (userUUID != null) {
            log.info("Request for user with UUID: {}", userUUID);
            if (sessionMap.get(userUUID) != null) {
                if (sessionMap.get(userUUID).getExpireDate().isAfter(LocalDateTime.now())) {
                    return Optional.of(sessionMap.get(userUUID).getUser());
                } else {
                    sessionMap.remove(userUUID);
                }
            }
        }
        log.info("Returned empty optional without user");
        return Optional.empty();
    }

    boolean checkPassword(User user, String passwordFromAuthRequest) {
        String salt = user.getSalt();
        String hashPassword = DigestUtils.sha256Hex(salt.concat(passwordFromAuthRequest));
        return user.getPassword().equals(hashPassword);
    }

    @Scheduled(initialDelayString = "${clean.session.list.time.interval}", fixedRateString = "${clean.session.list.time.interval}")
    public void cleanSessionMap() {
        log.info("Clean session list");
        sessionMap.entrySet().removeIf(session -> session.getValue().getExpireDate().isBefore(LocalDateTime.now()));
    }
}
