package org.learn.springsecuritydemo.repositories;

import org.learn.springsecuritydemo.entities.SessionEntity;
import org.learn.springsecuritydemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    List<SessionEntity> findByUser(User user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);

}
