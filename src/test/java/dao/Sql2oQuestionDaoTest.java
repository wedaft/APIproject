package dao;

import models.Question;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Sql2oQuestionDaoTest {
    private Sql2oUserDao userDao;
    private Sql2oQuestionDao questionDao;
    private Connection conn; //must be sql2o class conn

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        questionDao = new Sql2oQuestionDao(sql2o);
        userDao = new Sql2oUserDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void add_InstantiatesNewQuestion() throws Exception {
        Question test1 = setupTestQuestion();
        assertTrue(test1 instanceof Question);
    }

    @Test
    public void getAll_RetrieveAllQuestions_2() throws Exception {
        Question testQuestion  = setupTestQuestion();
        questionDao.add(testQuestion);
        Question testQuestion2  = setupTestQuestion2();
        questionDao.add(testQuestion2);
        assertEquals(2,questionDao.getAll().size());
    }

    @Test
    public void findById_FindASpecificQuestion_SnickersOrTwix() throws Exception {
        Question testQuestion2  = setupTestQuestion2();
        questionDao.add(testQuestion2);
        Question foundQuestion = questionDao.findById(1);
        assertEquals("Snickers or Twix?",foundQuestion.getPrompt());
    }

    @Test
    public void getAllUsersThatAnsweredQuestionReturnsCorrectly() throws Exception {
        Question testQuestion  = setupTestQuestion();
        questionDao.add(testQuestion);

        User testUser = setupTestUser();
        userDao.add(testUser);
        User testUser2 = setupTestUser2();
        userDao.add(testUser2);

        questionDao.addQuestionToUser(testUser,testQuestion);
        questionDao.addQuestionToUser(testUser2,testQuestion);

        User[] users = {testUser, testUser2};

        assertEquals(questionDao.getAllUsersThatAnsweredQuestion(testQuestion.getId()), Arrays.asList(users));
    }


    //helper method
    public static User setupTestUser (){
        return new User("Trevor Gill", 30, "male", 24, 32);
    }
    public static User setupTestUser2 (){
        return new User("Stuart Gill", 34, "male", 26, 35);
    }
    public static Question setupTestQuestion(){
        return new Question("Flight or invisibility?");
    }
    public static Question setupTestQuestion2 (){
        return new Question("Snickers or Twix?");
    }

}