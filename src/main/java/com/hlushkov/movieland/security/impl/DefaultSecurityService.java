package com.hlushkov.movieland.security.impl;

import com.hlushkov.movieland.common.request.AuthRequest;
import com.hlushkov.movieland.dao.UserDao;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultSecurityService implements SecurityService {
    private final UserDao jdbcUserDao;
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();
    @Value("${session.max.age}")
    private long sessionMaxAge;

    @Override
    public Optional<Session> login(AuthRequest authRequest) {

        Optional<User> optionalUser = jdbcUserDao.findByEmail(authRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String salt = user.getSalt();
            String hashPassword = DigestUtils.sha256Hex(salt.concat(authRequest.getPassword()));

            if (user.getPassword().equals(hashPassword)) {

                Session session = Session.builder()
                        .user(user)
                        .userUUID(UUID.randomUUID().toString())
                        .expireDate(LocalDateTime.now().plusSeconds(sessionMaxAge))
                        .build();

                sessionList.add(session);
                return Optional.of(session);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean removeSession(String userUUID) {
        return sessionList.removeIf(session -> session.getUserUUID().equals(userUUID));
    }

    @Override
    public Optional<User> getUserByUUID(String userUUID) {
        for (Session session : sessionList) {
            if (session.getUserUUID().equals(userUUID)) {
                return Optional.of(session.getUser());
            }
        }
        return Optional.empty();
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${clean.session.list.time.interval}", fixedRateString = "${clean.session.list.time.interval}")
    public void cleanSessionList() {
        log.info("Clean session list");
        sessionList.removeIf(session -> session.getExpireDate().isBefore(LocalDateTime.now()));
    }
}
