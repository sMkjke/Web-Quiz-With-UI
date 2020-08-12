//package engine.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Repository;
//
//
//import javax.persistence.*;
//import java.util.Collections;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//
//
//@Repository
//public class JPAQuizDAO implements DAO<Quiz> {
//
//    @PersistenceUnit
//    private EntityManagerFactory entityManager;
//
//
//    @Override
//    public Quiz get(int id) {
//
//        EntityManager em = entityManager.createEntityManager();
//        Optional<Quiz> optionalQuiz = Optional.ofNullable(em.find(Quiz.class, id));
//        if (optionalQuiz.isEmpty()) {
//            new ResponseEntity<>(HttpStatus.OK);
//        }
//        else {
//            new ResponseEntity<>(optionalQuiz.get(), HttpStatus.OK);
//        }
//        return optionalQuiz.get();
//    }
//
//    @Override
//    public List<Quiz> getAll() {
//        EntityManager em = entityManager.createEntityManager();
//            List<Quiz> quizzes = em.createQuery("Select t from Quiz t")
//                .getResultList();
//        em.close();
//        return quizzes;
//    }
//
//
//    @Override
//    public Quiz save(Quiz quiz) {
//        EntityManager em = entityManager.createEntityManager();
//        em.getTransaction().begin();
//        em.persist(quiz);
//        em.getTransaction().commit();
//        em.close();
//        return quiz;
//    }
//
//    public QuizResult evaluateQuiz(int id, QuizAnswer answer) {
//
//        int[] originalAnswer = get(id).getAnswer();
//        int[] providedAnswer = answer.getAnswer();
//
//        if (originalAnswer.length != providedAnswer.length)
//            return new QuizResult(true, "Congratulations, you're right!");
//        for (int i = 0; i < originalAnswer.length; i++) {
//            if (originalAnswer[i] != providedAnswer[i])
//                return new QuizResult(false, "Wrong answer! Please, try again.");
//        }
//        return new QuizResult(true, "Congratulations, you're right!");
//    }
//
////    private void executeInsideTransaction(Consumer<EntityManager> action) {
////        EntityManager em = entityManager.createEntityManager();
////        EntityTransaction tx = em.getTransaction();
////        try {
////            tx.begin();
////            action.accept(em);
////            tx.commit();
////        } catch (RuntimeException e) {
////            tx.rollback();
////            throw e;
////        }
////    }
//}
