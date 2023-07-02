package com.zybooks.studyhelper;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import com.zybooks.studyhelper.model.Question;

public class QuestionEditActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_ID = "com.zybooks.studyhelper.question_id";
    public static final String EXTRA_SUBJECT_ID = "com.zybooks.studyhelper.subject_id";

    private EditText mQuestionEditText;
    private EditText mAnswerEditText;
    private long mQuestionId;
    private Question mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);

        mQuestionEditText = findViewById(R.id.question_edit_text);
        mAnswerEditText = findViewById(R.id.answer_edit_text);

        findViewById(R.id.save_button).setOnClickListener(view -> saveButtonClick());

        // Get question ID from QuestionActivity
        Intent intent = getIntent();
        mQuestionId = intent.getLongExtra(EXTRA_QUESTION_ID, -1);

        if (mQuestionId == -1) {
            // Add new question
            mQuestion = new Question();
            mQuestion.setSubjectId(intent.getLongExtra(EXTRA_SUBJECT_ID, 0));

            setTitle(R.string.add_question);
        }
        else {
            // TODO: Display existing question from ViewModel
            mQuestion = new Question();

            setTitle(R.string.edit_question);
        }
    }

    private void updateUI() {
        mQuestionEditText.setText(mQuestion.getText());
        mAnswerEditText.setText(mQuestion.getAnswer());
    }

    private void saveButtonClick() {
        mQuestion.setText(mQuestionEditText.getText().toString());
        mQuestion.setAnswer(mAnswerEditText.getText().toString());

        // TODO: Save new or existing question
        if (mQuestionId == -1) {

        }
        else {

        }

        // Send back OK result
        setResult(RESULT_OK);
        finish();
    }
}