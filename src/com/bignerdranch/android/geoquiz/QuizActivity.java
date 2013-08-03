package com.bignerdranch.android.geoquiz;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private static final String IS_CHEATER = "isCheater";
	
	private Button mTrueButton;
	private Button mFalseButton;
	private ImageButton mNextButton;
	private ImageButton mPreviousButton;
	private TextView mQuestionTextView;
	private Button mCheatButton;
	private TextView mApiLevelTextView;
	private boolean mIsCheater;
	
	
	private TrueFalse[] mQuestionBank = new TrueFalse[] {
		new TrueFalse(R.string.question_oceans, true),
		new TrueFalse(R.string.question_mideast, false),
		new TrueFalse(R.string.question_africa, false),
		new TrueFalse(R.string.question_americas, true),
		new TrueFalse(R.string.question_asia, true),
	};
	
	private int mCurrentIndex = 0;
	
	private void updateQuestion() {
		int question = mQuestionBank[mCurrentIndex].getQuestion();
		mQuestionTextView.setText(question);
	}

	private void checkAnswer(boolean userPressedTrue) {
		boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
		
		int messageResId = 0;
		
		if (mIsCheater) {
			messageResId = R.string.judgment_toast;
		} else {
			if (userPressedTrue == answerIsTrue) {
				messageResId = R.string.correct_toast;
			} else {
				messageResId = R.string.incorrect_toast;
			}
		}

		
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
		     .show();
	}
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Log.d(TAG, "onCreate(Bundle) called");	
		setContentView(R.layout.activity_quiz);
		
		// example of dealing with compatibility issues (target api annotation waves off lint)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("Bodies of Water");
		}
		
		mQuestionTextView = (TextView)findViewById(R.id.question_text_view);		
		
		mTrueButton = (Button)findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				checkAnswer(true);
			}
		});
		mFalseButton = (Button)findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				checkAnswer(false);
			}
		});
		
		mPreviousButton = (ImageButton)findViewById(R.id.previous_button);
		mPreviousButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				if (mCurrentIndex == 0) {
					mCurrentIndex = mQuestionBank.length -1;
				} else {
					mCurrentIndex = (mCurrentIndex -1) % mQuestionBank.length;
				}
				
				updateQuestion();
			}
		});
		
		mNextButton = (ImageButton)findViewById(R.id.next_button);
		mNextButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex +1) % mQuestionBank.length;
				updateQuestion();
			}
		});
		
		mCheatButton = (Button)findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// start cheat activity
				Intent i = new Intent(QuizActivity.this, CheatActivity.class);
				boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
				i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
				startActivityForResult(i, 0);
			}
		});
		mApiLevelTextView = (TextView)findViewById(R.id.api_level);
		mApiLevelTextView.setText("API Level: " + Build.VERSION.RELEASE);
		
		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
			mIsCheater = savedInstanceState.getBoolean(IS_CHEATER, false);
		}
		
		updateQuestion();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.i(TAG, "onSaveInstanceState");
		savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
		savedInstanceState.putBoolean(IS_CHEATER, mIsCheater);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart() called");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause() called");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume() called");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop() called");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy() called");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

}
