package org.learn.springsecuritydemo.services;

import lombok.RequiredArgsConstructor;
import org.learn.springsecuritydemo.entities.SessionEntity;
import org.learn.springsecuritydemo.entities.User;
import org.learn.springsecuritydemo.repositories.SessionRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;

    public void generateNewSession(User user, String refreshToken){
        List<SessionEntity> sessions = sessionRepository.findByUser(user);
        if(sessions.size() == SESSION_LIMIT){
            sessions.sort(Comparator.comparing(SessionEntity::getLastUsedAt));

            SessionEntity leastRecentlyUsedSession = sessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        SessionEntity session = SessionEntity.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();

        sessionRepository.save(session);
    }

//    public boolean validateSession(String refreshToken){
//        if(sessionRepository.findByRefreshToken(refreshToken) == null){
//            throw new SessionAuthenticationException("Session not found for refresh token - "+refreshToken);
//        }
//
//        return true;
//    }

    public void validateSession(String refreshToken){
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow( () ->
                        new SessionAuthenticationException("Session not found for refresh token - "+refreshToken)
                        );
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
