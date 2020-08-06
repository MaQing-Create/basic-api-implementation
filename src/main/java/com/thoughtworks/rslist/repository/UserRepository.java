package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    List<UserEntity> findAll();

    UserEntity getUsersByUserId(Integer userId);

    @Transactional
    void deleteByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    boolean existsByUserName(String userName);

//    @Query("select u from user as u where u.user_id between ?1 and ?2")
//    List<UserEntity> getUsersBetweenUserId(Integer start, Integer end);
//
////    List<UserEntity> getUsersBetweenUserId(Integer start, Integer end);
}