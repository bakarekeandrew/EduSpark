package rw.auca.EduSpark.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.auca.EduSpark.model.MyAppUser;
import rw.auca.EduSpark.model.UserRole;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser(MyAppUser user);
}

