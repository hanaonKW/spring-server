package hanaon.AiAssistant.repository;

import hanaon.AiAssistant.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
