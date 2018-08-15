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

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random r =new Random();
        String w="";
        if(prefix.equals("")){
            return words.get(r.nextInt(words.size()));
        }
        else {
            int beg, end=words.size(), mid, flag=0;
            beg=0;

            while (beg<end){
                mid=(beg+end)/2;
                String subWord;
                Log.e("Inside while",String.valueOf(beg));
                if(words.get(mid).length()<prefix.length()){
                    subWord=words.get(mid);
                } else {
                    subWord = words.get(mid).substring(0, prefix.length());
                }

                if (prefix.compareTo(subWord) == 0) {
                    w = words.get(mid);
                    flag = 1;
                    Log.e("GotWord:",w);
                    break;
                } else if (prefix.compareTo(subWord) < 0) {
                    end = mid - 1;
                } else {
                    beg = mid + 1;
                }
            }
            if(flag==1){
                return w;
            }
            else {
                Log.e("Insidewhileelse:","No word found");
                return "";
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
