package com.example.polls.repository;

import com.example.polls.model.User;
import com.example.polls.payload.DeletePollRequest;
import com.example.polls.payload.UpdateProfileRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User s set  s.name = CASE WHEN :#{#userRequest.name} = '' THEN :#{#userRequest.oldName} ELSE :#{#userRequest.name} END," +
            " s.email = CASE WHEN :#{#userRequest.email} = '' THEN :#{#userRequest.oldEmail} ELSE :#{#userRequest.email} END " +
            " WHERE s.username = :#{#userRequest.oldUsername} and s.email = :#{#userRequest.oldEmail} ")
    int updateUserProfile(@Param("userRequest") UpdateProfileRequest userRequest);

    // STARI NACIN KAKO SAM SLAO VIŠE PARAMETRI OD STRINGOVI
    //@Query("UPDATE User s SET s.name = :name  WHERE s.username = :oldUsername")
    //int updateUserProfile(@Param("name") String name, @Param("oldUsername") String oldUsername);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.poll.id = :#{#deletePollRequest.pollId} ")
    int deleteVotes(@Param("deletePollRequest") DeletePollRequest deletePollRequest);

    @Modifying
    @Transactional
    @Query("DELETE FROM Choice c WHERE  c.poll.id = :#{#deletePollRequest.pollId} ")
    int deleteChoices(@Param("deletePollRequest") DeletePollRequest deletePollRequest);

    @Modifying
    @Transactional
    @Query("DELETE FROM Poll p WHERE p.id = :#{#deletePollRequest.pollId} ")
    int deletePoll(@Param("deletePollRequest") DeletePollRequest deletePollRequest);

}
