package com.example.qaservice.repository;

import com.example.qaservice.data.entity.Poll;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends CrudRepository<Poll, Long> {

    @Modifying
    @Transactional
    @Query(value = "WITH Pdeletes AS (DELETE FROM poll p WHERE p.id = :id RETURNING p.id AS id), " +
        "ADeletes AS (DELETE FROM question q WHERE q.poll_id IN (SELECT id FROM Pdeletes)) " +
        "DELETE FROM answer a WHERE a.poll_id IN (SELECT id FROM Pdeletes)", nativeQuery = true)
    void deletePoll(@Param("id") Long id);

    List<Poll> findAllByEndDateIsAfter(Date date);

    Optional<Poll> findByEndDateIsAfterAndId(Date date, Long id);

}
