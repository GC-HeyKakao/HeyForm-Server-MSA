package heykakao.HeyForm.repository;

import heykakao.HeyForm.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select a from Answer a where a.survey_id = ?1")
    List<Answer> findBySurvey_id(Long survey_id);

    @Query("select a from Answer a where a.user_id = ?1")
    List<Answer> findByUser_id(Long user_id);

    @Query("select a from Answer a where a.question_id = ?1")
    List<Answer> findByQuestion_id(Long question_id);










}