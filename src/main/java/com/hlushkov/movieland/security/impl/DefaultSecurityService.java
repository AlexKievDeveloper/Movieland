package com.hlushkov.movieland.security.impl;

import com.hlushkov.movieland.dao.jdbc.JdbcUserDao;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
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
    private final JdbcUserDao jdbcUserDao;
    private final List<Session> sessionList = new CopyOnWriteArrayList<>();
    @Value("${session.max.age}")
    private long sessionMaxAge;

    @Override
    public Optional<Session> login(String email, String password) {
        try {
            Optional<User> optionalUser = jdbcUserDao.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String salt = user.getSalt();
                String hashPassword = DigestUtils.sha256Hex(salt.concat(password));

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
        } catch (DataAccessException e) {
            log.info("Unsuccessful user authentication with email: {}", email);
            return Optional.empty();
        }

        return Optional.empty();
    }

    @Override
    public boolean removeSession(String userUUID) {
        return sessionList.removeIf(session -> session.getUserUUID().equals(userUUID));
    }

    @Override
    public Optional<Session> getSession(String userUUID) {
        for (Session session : sessionList) {
            if (session.getUserUUID().equals(userUUID)) {
                return Optional.of(session);
            }
        }
        return Optional.empty();
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${clean.session.list.time}", fixedRateString = "${clean.session.list.time}")
    public void cleanSessionList() {
        log.info("Clean session list");
        sessionList.removeIf(session -> session.getExpireDate().isBefore(LocalDateTime.now()));
    }
}
