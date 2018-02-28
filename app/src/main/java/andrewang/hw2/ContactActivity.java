// Andrew Ang
// Professor Dingler
// HW 2 Contacts List
package andrewang.hw2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContactActivity extends AppCompatActivity {
    //for logging
    private final String TAG = "contacts";
    private final Contacts[] mContacts = new Contacts[] {
            new Contacts(R.string.pikachu_name, R.drawable.pikachu,"Electric"),
            new Contacts(R.string.bulbasaur_name, R.drawable.bulbasaur, "Grass"),
            new Contacts(R.string.charmander_name, R.drawable.charmander, "Fire"),
            new Contacts(R.string.squirtle_name, R.drawable.squirtle, "Water")
    }; // create an array to store all of the information
    //filename for location data
    private final String FILENAME = "contact_data";
    //key to save mCurrentIndex in Bundle
    private static final String KEY_CONTACT_INDEX = "contact_index";
    private int mCurrentIndex = 0;
    private TextView nameTextView;
    private TextView typeTextView;
    private ImageView imgView;
    private Button prevBtn;
    private Button nextBtn;
    private EditText nickNameEditView;
    private TextView levelTextView;
    private TextView nickNameActual;
    private SeekBar pokemonLevelBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //Create all of the instances of the textviews, pictureviews, buttons, etc.
        nameTextView = (TextView) findViewById(R.id.NameTextViewActual);
        typeTextView = (TextView) findViewById(R.id.TypeTextViewActual);
        imgView = (ImageView) findViewById(R.id.imgView);
        prevBtn = (Button) findViewById(R.id.PrevBtn);
        nextBtn = (Button) findViewById(R.id.NextBtn);
        nickNameEditView = (EditText) findViewById(R.id.NickNameEditText);
        levelTextView = (TextView) findViewById(R.id.LevelBarTextView);
        nickNameActual = (TextView) findViewById(R.id.NickNameActual);
        pokemonLevelBar = (SeekBar) findViewById(R.id.LevelSeekBar);
        try(FileInputStream in = openFileInput(FILENAME)) {
            //load locations from file, if file was written
            String line, otherLine;
            char next, otherNext;

            //loop over the 4 lines in the file
            for(int i = 0; i < 4; i++) {
                line = "";
                otherLine = "";
                // create the nicknames
                while ((next = (char) in.read()) != '\n') {
                    line += next;
                }
                // create the level
                while ((otherNext = (char) in.read()) != '\n') {
                    otherLine += otherNext;
                }
                mContacts[i].setmNickName(line); // get the info from the file and set it
                mContacts[i].setmLevel(Integer.parseInt(otherLine)); // get
                Log.d(TAG, "Read the following location from file: " + line);
            }
            nickNameActual.setText(mContacts[mCurrentIndex].getmNickName());
            pokemonLevelBar.setProgress(mContacts[mCurrentIndex].getmLevel());
            levelTextView.setText(Integer.toString(pokemonLevelBar.getProgress()));
            in.close();
        } catch (java.io.FileNotFoundException fnfe) {
            Log.d(TAG, "FileNotFoundException when trying to load file" + fnfe);
        } catch(java.io.IOException ioe) {
            Log.d(TAG, "IOException when trying to load file" + ioe);
        }

        //update with saved state when re-creating this activity
        if(savedInstanceState != null)
            mCurrentIndex = savedInstanceState.getInt(KEY_CONTACT_INDEX, 0);

        // set up the items for the "intro screen"
        nameTextView.setText(mContacts[mCurrentIndex].getmNameResId());
        imgView.setImageResource(mContacts[mCurrentIndex].getmImageResId());
        typeTextView.setText(mContacts[mCurrentIndex].getmType());

        // Seek Bar
        pokemonLevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressValue;
            @Override // when something is changed change the text
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressValue = i;
                levelTextView.setText(Integer.toString(progressValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override // once stopped saved the info
            public void onStopTrackingTouch(SeekBar seekBar) {
                // save the information
                if(!levelTextView.getText().toString().equals("0"))
                {
                    mContacts[mCurrentIndex].setmLevel(Integer.parseInt(levelTextView.getText().toString()));
                }
            }
        });

        // When the previous button is pressed
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Save the current info on the screen
                saveInfo();
                mCurrentIndex = (mCurrentIndex - 1);

                //ensures we wrap back to index 3 (instead of -1)
                if(mCurrentIndex == -1)
                    mCurrentIndex = 3;
                //update all text fields and the image view based on current state
                update();
            }
        });
        // when next button is clicked
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //% ensures we wrap back to index 0
                //Save the current info on the screen
                saveInfo();
                mCurrentIndex = (mCurrentIndex + 1) % 4;
                //update all text fields and the image view based on current state
                update();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");

        //when app is destroyed, save file info
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);

        //write each contact location to a new line
        for(int i = 0; i < 4; i++) {
            outputStream.write((mContacts[i].getmNickName() + '\n').getBytes());
            outputStream.write((Integer.toString(mContacts[i].getmLevel()) + '\n').getBytes());
            Log.d(TAG, "Writing Nickname: " + mContacts[i].getmNickName() + '\n');
            Log.d(TAG, "Writing Level: " + mContacts[i].getmLevel() + '\n');
        }

        outputStream.close();
        } catch (FileNotFoundException fnfe) {
            Log.d(TAG, "FileNotFound Exception when trying to write output");
        } catch (IOException ioe) {
            Log.d(TAG, "IOException when trying to write output");
        }
}
    //we use onSaveInstanceState to keep track of current contact (mainly
    // to preserve it across rotations)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(KEY_CONTACT_INDEX, mCurrentIndex);
    }

    // update all of the information of the pokemon
    private void update() {
        //set the ImageView with the current image and the TextView with the current name
        //recall: we are saving resource IDs in the Contact class
        nameTextView.setText(mContacts[mCurrentIndex].getmNameResId());
        imgView.setImageResource(mContacts[mCurrentIndex].getmImageResId());
        typeTextView.setText(mContacts[mCurrentIndex].getmType());
        //clear the edit text
        nickNameEditView.setText("");
        //set the name of the pokemon
        nickNameActual.setText(mContacts[mCurrentIndex].getmNickName());
        // set the actual seek bar to current value stored
        pokemonLevelBar.setProgress(mContacts[mCurrentIndex].getmLevel());

    }

    // Save the name of the pokemon
    private void saveInfo() {
        if(!nickNameEditView.getText().toString().equals("")) // if there is nothing in the text do not save it
        {
            mContacts[mCurrentIndex].setmNickName(nickNameEditView.getText().toString());
        }

    }
}
