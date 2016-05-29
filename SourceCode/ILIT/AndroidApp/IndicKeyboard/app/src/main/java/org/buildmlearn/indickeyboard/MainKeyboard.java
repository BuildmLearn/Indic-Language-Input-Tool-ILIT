package org.buildmlearn.indickeyboard;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;

public class MainKeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener{
    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard extendedKeyboard;

    private static final int SPACE_KEY = 32;
    private static final int DELIMITER_KEY =124 ;
    public static final int adaptive_dependent=-102;
    public static final int adaptive_consonantFillsCombinations=-101;
    public static final int adaptive_consonantCombinations_to_vowel=-100;
    public static final int adaptive_vowel_to_consonantCombinations=-99;
    public static final int extended_adaptive =-98;
    public static final int main_to_extended_consonant = -94;
    public static final int extended_consonant_to_main=-93;
    public int last_consonant_pressed=2325;
    public static int first_consonant;
    public static boolean currentKeyboardisMain=true; //initial
    public static boolean currentViewHasVowel=true; //initial
    public static  int currentEventTriggered;
    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.mainkeyboard, null);
        keyboard = new Keyboard(this, R.xml.hindi); //right now just setting it directly to hindi
        extendedKeyboard=new Keyboard(this,R.xml.hindi_extended_consonants);
        kv.setProximityCorrectionEnabled(false);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {


    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;

            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                currentEventTriggered=Keyboard.KEYCODE_SHIFT;
                break;

            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                currentEventTriggered=Keyboard.KEYCODE_DONE;
                break;

            case SPACE_KEY :
                /* space key code here
                 * To Add : Ability to Change Language and Keyboard here */
                ic.commitText(" ",1);
                currentEventTriggered=SPACE_KEY;
                break;
            /*
            Delimiter Key.
            Just like Full Stop in Latin.
            In Devanagari |
            So addition of this would add 1 extra space before addition of next word.
             */
            case DELIMITER_KEY:
                ic.commitText("| ",1);
                currentEventTriggered=DELIMITER_KEY;
                break;

            /*

            Extend the Main Keyboard Consonants to Extend.
            Triggered by the forward arrow.
            Shows the remaining Consonant

            */
            case main_to_extended_consonant:
                kv.setKeyboard(extendedKeyboard);
                currentKeyboardisMain = false;
                changeAdaptive(main_to_extended_consonant); //Redraw
                kv.invalidateAllKeys();
                break;

            /*
            Current Keyboard is Extended Consonants.
            Load Main Keyboard.
            Triggered by prev arrow.

             */

            case extended_consonant_to_main:
                kv.setKeyboard(keyboard);
                currentKeyboardisMain = true;
                changeAdaptive(extended_consonant_to_main); //Redraw
                kv.invalidateAllKeys();
                break;

            /*
            Adaptive Area has Consonants right now.
            Load vowels in the Adaptive Area

             */
            case adaptive_consonantCombinations_to_vowel:
                changeAdaptive(adaptive_consonantCombinations_to_vowel);
                kv.invalidateAllKeys();
                currentViewHasVowel=true;
                break;
              /*
            Adaptive Area has Vowels right now.
            Load consonant combinations in the Adaptive Area .
             consonant is the current consonant pressed or the consonant last pressed.

             */
            case adaptive_vowel_to_consonantCombinations:


                changeAdaptive(adaptive_vowel_to_consonantCombinations);
                currentViewHasVowel=false;
                kv.invalidateAllKeys();
                break;


            default:

                    /*
                    Primary Code is the int unicoded code point value of char
                    Get the char value from the primaryCode
                                         */
                    char code = (char) primaryCode;

                    /*
                    Check if the char is a consonant of the Language
                    If yes, load its combinations in the Adaptive Area.

                     */
                    if (IsConsonant(primaryCode)) {
                        last_consonant_pressed = primaryCode;
                        changeAdaptive(adaptive_consonantFillsCombinations);
                        currentViewHasVowel=false;
                        kv.invalidateAllKeys();

                    }

                    /*
                    Check if the char is a dependent vowel of the Script.


                     */
                    if (IsDependentVowel(primaryCode) ) {

                        if((primaryCode==2306 || primaryCode==2307 ) && currentViewHasVowel) {

                                ic.commitText(String.valueOf((char) 2309), 1); //Add Devnagari A

                        }
                        boolean dontaddprevious =currentEventTriggered==adaptive_consonantFillsCombinations;

                        if(!dontaddprevious && !currentViewHasVowel) {
                            ic.commitText(String.valueOf((char) last_consonant_pressed), 1); //Add previous coz Adaptive lies open
                        }
                        currentEventTriggered=adaptive_dependent;

                    }


                    if (Character.isLetter(code) && caps) {
                        code = Character.toUpperCase(code);

                    }


                        ic.commitText(String.valueOf(code), 1); //finally add the key after all the processing

                    /* In case to deal with letters with multiple keycodes
                        String tempMultiple="";
                        if (keyCodes.length > 1) {
                            for (int c : keyCodes) {

                                if(c==-1) break;
                            tempMultiple=tempMultiple+ String.valueOf((char)c);
                            }
                        }
                     */


                }
        }

        /*
        Deals with the Adaptive Area : Events and changing the keys based on the events that triggered them


         */
        public void changeAdaptive(int eventcode) {


        currentEventTriggered=eventcode; //Set the Event

        first_consonant=currentKeyboardisMain ?2325 :2358; //static right now. Would make it dynamic


        int[] dependentVowels = new int[]{last_consonant_pressed, 2366, 2367, 2368, 2369, 2370, adaptive_vowel_to_consonantCombinations, 2375, 2376, 2379, 2380, 2306, 2307, extended_adaptive};
        int[] independentVowels =new int[]{2309, 2310, 2311, 2312, 2313, 2314, adaptive_consonantCombinations_to_vowel, 2319, 2320, 2323, 2324, 2306, 2307, extended_adaptive};

         //Get all the Keys of the keyboard. No other option!

         ArrayList<Keyboard.Key> keys = (ArrayList<Keyboard.Key>) kv.getKeyboard().getKeys();
            //Loop through all the keys
        for (Keyboard.Key k : keys) {


            if (k.codes[0] == first_consonant && keys.indexOf(k) != 0) {
                //got all the top keys before the first consonant
                //And the key is not the first one
                break; //break the loop

            }
            //event : change vowels in adaptive to consonant
            //change the key
            else if (k.codes[0] == adaptive_vowel_to_consonantCombinations) {

                k.codes[0] = adaptive_consonantCombinations_to_vowel; //switch
                k.label=null;
                k.icon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.rafar);

            }
            //event : change barkhadi in adaptive area to vowels

            else if (k.codes[0] == adaptive_consonantCombinations_to_vowel) {
                k.codes[0] = adaptive_vowel_to_consonantCombinations; //change the code of the key
                k.icon= null; //remove the icon here
                k.label="अ"; //set the new label


            }
            //event : Forward Arrow pressed in the Adaptive Area

            else if (k.codes[0] == extended_adaptive) {
                //To DO
            }

            else {

                if (eventcode == adaptive_consonantFillsCombinations || eventcode ==adaptive_vowel_to_consonantCombinations ||
                        ((eventcode==main_to_extended_consonant || eventcode==extended_consonant_to_main) && !currentViewHasVowel)) {
                  //These events will mean that you need barakhdi in the Adaptive Area.

                    k.codes[0] = dependentVowels[keys.indexOf(k)]; //Get the Dependent Vowels
                    k.label = String.valueOf((char) last_consonant_pressed); //Add based on the consonant
                    if (k.codes[0] != last_consonant_pressed) {
                        k.label =k.label +String.valueOf((char) k.codes[0]);
                    }

                }
                else if (eventcode == adaptive_consonantCombinations_to_vowel || ((eventcode==main_to_extended_consonant || eventcode==extended_consonant_to_main) && currentViewHasVowel)) {
                    //This event means you need Vowel Combinations

                    k.codes[0] = independentVowels[keys.indexOf(k)];
                    if (k.codes[0] == 2306 || k.codes[0] == 2307) {
                        //Add additional Devanagari AA
                        k.label = String.valueOf((char) 2309) + String.valueOf((char) k.codes[0]);
                    }
                    else {
                        //Other keys. Simply create the label from the codes
                        k.label = String.valueOf((char) k.codes[0]);
                    }

                }

                else {
                    //when in case to add further events
                }
            }

        }

    }

    /*
    Checks if the Key is Consonant of the Script or not

        */
    public boolean IsConsonant(int code)
    {
        //As per Devanagari IndicSyllabic UCD Documentation

        //0x915 to 0x939 range 1 Devanagari Consonant ...0x958 to 0x95F range 2 Devanagari Consonant
        return (code >= 2325 && code <=2361) || (code>=2393 && code <=2399);


    }

    /*
    Checks if the key is dependent Vowel or not

     */

    public boolean IsDependentVowel(int code)
    {
        //As per Devanagari IndicSyllabic UCD Documentation
      return (code==2306) || (code==2307)|| (code==2362) || code==2363 || (code >=2366 && code <= 2383) || (code >=2389 && code <= 2391) || (code ==2402 )||( code == 2403);

    }
    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
