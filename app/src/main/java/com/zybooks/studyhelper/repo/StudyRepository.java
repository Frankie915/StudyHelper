package com.zybooks.studyhelper.repo;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.room.Room;
import com.zybooks.studyhelper.model.Question;
import com.zybooks.studyhelper.model.Subject;
import java.util.List;

public class StudyRepository {

    private static final String DATABASE_NAME = "study3.db";
    private static StudyRepository mStudyRepo;
    private final SubjectDao mSubjectDao;
    private final QuestionDao mQuestionDao;

    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService mDatabaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static StudyRepository getInstance(Context context) {
        if (mStudyRepo == null) {
            mStudyRepo = new StudyRepository(context);
        }
        return mStudyRepo;
    }

    private StudyRepository(Context context) {

        RoomDatabase.Callback databaseCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                mDatabaseExecutor.execute(() -> addStarterData());
            }
        };

        StudyDatabase database = Room.databaseBuilder(context, StudyDatabase.class, DATABASE_NAME)
                .addCallback(databaseCallback)
                .build();

        mSubjectDao = database.subjectDao();
        mQuestionDao = database.questionDao();

        /*
        if (mSubjectDao.getSubjects().isEmpty()) {
            addStarterData();
        }*/
    }

    private void addStarterData() {
        Subject subject = new Subject("Math");
        long subjectId = mSubjectDao.addSubject(subject);

        Question question = new Question();
        question.setText("What is 2 + 3?");
        question.setAnswer("2 + 3 = 5");
        question.setSubjectId(subjectId);
        mQuestionDao.addQuestion(question);

        question = new Question();
        question.setText("What is pi?");
        question.setAnswer("The ratio of a circle's circumference to its diameter.");
        question.setSubjectId(subjectId);
        mQuestionDao.addQuestion(question);

        subject = new Subject("History");
        subjectId = mSubjectDao.addSubject(subject);

        question = new Question();
        question.setText("On what date was the U.S. Declaration of Independence adopted?");
        question.setAnswer("July 4, 1776");
        question.setSubjectId(subjectId);
        mQuestionDao.addQuestion(question);

        subject = new Subject("Computing");
        mSubjectDao.addSubject(subject);
    }

    public void addSubject(Subject subject) {
        mDatabaseExecutor.execute(() -> {
            long subjectId = mSubjectDao.addSubject(subject);
            subject.setId(subjectId);
        });
    }

    public LiveData<Subject> getSubject(long subjectId) {
        return mSubjectDao.getSubject(subjectId);
    }

    public LiveData<List<Subject>> getSubjects() {
        return mSubjectDao.getSubjects();
    }

    public void deleteSubject(Subject subject) {
        mDatabaseExecutor.execute(() -> mSubjectDao.deleteSubject(subject));
    }

    public LiveData<Question> getQuestion(long questionId) {
        return mQuestionDao.getQuestion(questionId);
    }

    public LiveData<List<Question>> getQuestions(long subjectId) {
        return mQuestionDao.getQuestions(subjectId);
    }

    public void addQuestion(Question question) {
        mDatabaseExecutor.execute(() -> {
            long questionId = mQuestionDao.addQuestion(question);
            question.setId(questionId);
        });
    }

    public void updateQuestion(Question question) {
        mDatabaseExecutor.execute(() -> mQuestionDao.updateQuestion(question));
    }

    public void deleteQuestion(Question question) {
        mDatabaseExecutor.execute(() -> mQuestionDao.deleteQuestion(question));
    }
}