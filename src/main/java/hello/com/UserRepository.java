package hello.com;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Integer> {
    public User findByUserID(Long UserID);
}
