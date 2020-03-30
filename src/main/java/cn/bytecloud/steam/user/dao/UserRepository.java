package cn.bytecloud.steam.user.dao;

import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.entity.UserType;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByName(String asdf);

    List<User> findByUsername(String username);

    User findFirstByType(UserType root);

    User findFirstByUsername(String idCard);

    List<User> findByType(UserType user);

    List<User> findByType(UserType user, Sort orders);
}
