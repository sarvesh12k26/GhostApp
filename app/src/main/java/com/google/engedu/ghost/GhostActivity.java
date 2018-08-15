/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    TextView text;
    TextView label;
    SimpleDictionary sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            sd = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        label = (TextView) findViewById(R.id.gameStatus);
        text = (TextView) findViewById(R.id.ghostText);
        String currentString=text.getText().toString();
        Log.e("Incomputerturn:",currentString);
        if(currentString.length()>=4 && sd.isWord(currentString)){
            Toast.makeText(this,"Computer wins",Toast.LENGTH_SHORT).show();

            onStart(null);
        }
        else{
            String compWord=sd.getAnyWordStartingWith(currentString);
            if (compWord.equals("")){
                Toast.makeText(this,"Computer wins",Toast.LENGTH_SHORT).show();
                onStart(null);
            }
            else {
                currentString=currentString+compWord.charAt(currentString.length());
                text.setText(currentString);
            }
        }


        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    public void onChallenge(View view){
        String currentWord;
        currentWord=text.getText().toString();
        String w="";
        if(sd.isWord(currentWord)){
            Toast.makeText(this, "User wins", Toast.LENGTH_SHORT).show();
        }
        else {
            w=sd.getAnyWordStartingWith(currentWord);
            if(w.equals("")){
                Toast.makeText(this, "User wins", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Computer wins", Toast.LENGTH_SHORT).show();
            }
        }
        onStart(null);
    }



    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        char pressedKey = (char) event.getUnicodeChar();
        String currentString=text.getText().toString();
        //Log.e("String:",currentString);
        if (pressedKey>='a' && pressedKey <='z'){
            currentString=currentString+pressedKey;
            Log.e("OnkeyupCurrentstring:",currentString);
            text.setText(currentString);
        }
        //text.setText(currentString);
        Log.e("String:",currentString);

        userTurn = false;
        computerTurn();

        return super.onKeyUp(keyCode, event);
    }
}
