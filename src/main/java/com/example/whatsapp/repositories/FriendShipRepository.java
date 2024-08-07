package com.example.whatsapp.repositories;

import com.example.whatsapp.enums.FriendShipStatus;
import com.example.whatsapp.models.FriendShip;
import com.example.whatsapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    void deleteByUserOneAndUserTwo(User userOne, User userTwo);

    @Query("select fs from FriendShip fs where (fs.userOne = ?1 or fs.userTwo = ?2) " +
            " and fs.friendShipStatus != ?3")
    List<FriendShip> findAllByUserOneOrUserTwo(User userOne, User userTwo, FriendShipStatus status);
    FriendShip findByUserOneAndUserTwo(User userOne, User userTwo);

    FriendShip findByUserOneAndUserTwoOrUserTwoAndUserOne(
            User userOne1, User userTwo1, User userOne2, User userTwo2);

    List<FriendShip> findAllByUserTwoAndFriendShipStatus(User userTwo, FriendShipStatus status);
}
