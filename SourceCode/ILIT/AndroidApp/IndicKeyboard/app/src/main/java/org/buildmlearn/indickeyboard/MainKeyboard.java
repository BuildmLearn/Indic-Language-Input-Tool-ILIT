package org.buildmlearn.indickeyboard;


import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class MainKeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {


    public static int first_consonant; //first consonant of the language
    public static boolean currentViewHasVowel; //Does the view has Vowel or Consonant Combinations in the adaptive area
    public static boolean currentAdaptive;//true if it has main else false-extended mode active
    public static int currentEventTriggered;//maintains current event triggered to use them further for appropriate reactions to the event.
    private static boolean changeLanguage; //Is the language changed? by InputMethodManager
    public int currentKeyboard; //which is the currentKeyboard
    public int last_consonant_pressed; //which was the last consonant pressed by user
    private CustomKeyboardView kv; //KeyboardView
    private LatinKeyboard keyboard; // keyboard
    private LatinKeyboard mainKeyboard;
    private LatinKeyboard extendedKeyboard; //extended keyboard
    private LatinKeyboard phonepad; //Phone pad keyboard for Numeric Enteries only
    private LatinKeyboard symbols; //Symbolic Keyboard
    private InputMethodManager mInputMethodManager; //InputMethodManager which will handle sub-ime and subtype Language method picker
    private InputConnection ic; //InputConnection
    private String language; //current language of the keyboard
    private String displayMode; //Maintains the Orientation of user's device for different layouts
    private boolean caps = false; //caps lock key

    @Override

    public void onCreate() {
        super.onCreate();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initialize();

    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        ic = getCurrentInputConnection();
        // setImeOptions(); will call this when having multiple languages to select from.
        String prevDisplayMode = displayMode;
        detectDisplayMode();
        if (displayMode != prevDisplayMode) {
            setInputView(onCreateInputView()); //Orientation changed
        }
    }

    @Override
    public View onCreateInputView() {


        detectDisplayMode();//detect the orientation
        initializeLanguage();//Gets the language at run time from the choosen InputMethodSubtype & sets it to language varible
        setMainKeyboardView();//create the mainkeyboard view and set it to keybaordview kv
        createKeyboards();//create main,extended,symbol and phonepad layouts
        keyboard=getCurrentKeyboard();
        kv.setProximityCorrectionEnabled(false);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
        checkForOrientationAdaptiveCorrection();
        return kv;
    }

    private LatinKeyboard getCurrentKeyboard()
    {
        LatinKeyboard keybd;
        switch(currentKeyboard)
        {
            case Constants.CurrentKeyboard_EXTENDED:
                keybd=extendedKeyboard;
                break;
            case Constants.CurrentKeyboard_PHONEPAD:
                keybd=phonepad;
                break;
            case Constants.CurrentKeyboard_SYMBOL:
                keybd=symbols;
                break;

            default:
                keybd=mainKeyboard;
                break;
        }
        if(keybd==mainKeyboard) //i.e either the resource was not there or it was default
        {
            currentKeyboard=Constants.CurrentKeyboard_MAIN;
        }
        return keybd;



    }

    private void checkForOrientationAdaptiveCorrection() {
        if (last_consonant_pressed != 0 && (currentKeyboard == Constants.CurrentKeyboard_MAIN || currentKeyboard==Constants.CurrentKeyboard_EXTENDED)) {

            /*  That means it came here after orientation changes
            Now we are gonna fire changeAdaptive because we want the user to have barakhdi
            which he was having before the orientation change
            even though he changed the layout completely by changing to landscape
            */
                changeAdaptive(Constants.orientation_retain_adaptive); //just to trigger the orientation;



        } else if (currentKeyboard == Constants.CurrentKeyboard_SYMBOL || currentKeyboard == Constants.CurrentKeyboard_PHONEPAD)
        {
            updateDigits(language);
        }
    }

    private void createKeyboards() {
        //public because you can override in case you have some other implementation to create the keyboards

        //Here is the main keyboard
        mainKeyboard = new LatinKeyboard(this, getKeyboardViewResourceId(false));

        //Create other keyboard layouts as well.
        //these will take appropriate layout and language and then generate the layout

        extendedKeyboard = getKeyboardFromRes(getKeyboardViewResourceId(true));
        phonepad = getKeyboardFromRes(getResourceId("phonepad"));
        symbols = getKeyboardFromRes(getResourceId("symbols"));
    }

    private void setMainKeyboardView() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        //int theme=settings.getInt("theme",R.layout.mainkeyboard);
        int theme=R.layout.mainkeyboard_dark;
        kv = (CustomKeyboardView) getLayoutInflater().inflate(theme, null);
    }


    private void initializeLanguage() {
        String code = mInputMethodManager.getCurrentInputMethodSubtype().getLocale();
        language = ImeUtilities.getLanguage(code);
        Log.d("lan", code + " " + language);
    }

    private void initialize() {
        currentViewHasVowel = true;
        currentAdaptive = true;
        currentKeyboard = Constants.CurrentKeyboard_MAIN;
    }

    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {

        if (kv != null) {
            kv.setSubtypeOnSpaceKey(subtype);
        }
        setInputView(onCreateInputView());

    }


    private void playClick(int keyCode) {
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

    public void onLongPress(Keyboard.Key key) {
        /*
        To implement to select multiple languages

        */
        if (key.codes[0] == Constants.SPACE_KEY) {
            Log.d("imm", String.valueOf(mInputMethodManager == null));
            //mInputMethodManager.showInputMethodPicker(); Will use to show various languages available
            changeLanguage = true;

        }

    }

    private LatinKeyboard getKeyboardFromRes(int resourceId) {
        if (resourceId != 0) {
            return new LatinKeyboard(this, resourceId);
        }
        return mainKeyboard; //keyboard shows the main keyboard. and it should be existing for other layouts to exist.
    }

    @Override
    public void onPress(int primaryCode) {


    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                if(!currentViewHasVowel){
                    //Delete will bring back the Vowel mode
                    currentViewHasVowel = true;
                    currentAdaptive=true; //start from beginning
                    changeAdaptive(Constants.adaptive_consonantCombinations_to_vowel);
                    kv.invalidateAllKeys();
                }
                break;

            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                currentEventTriggered = Keyboard.KEYCODE_SHIFT;
                break;

            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                currentEventTriggered = Keyboard.KEYCODE_DONE;
                break;

            case Constants.SPACE_KEY:
                /* space key code here
                 * To Add : Ability to Change Language and Keyboard here */
                if (!changeLanguage) {
                    ic.commitText(" ", 1);
                }
                currentEventTriggered = Constants.SPACE_KEY;
                break;
            /*
            Delimiter Key.
            Just like Full Stop in Latin.
            In Devanagari |
            So addition of this would add 1 extra space before addition of next word.
             */
            case Constants.DELIMITER_KEY:
                ic.commitText("| ", 1);
                currentEventTriggered = Constants.DELIMITER_KEY;
                break;

            /*

            Extend the Main Keyboard Consonants to Extend.
            Triggered by the forward arrow.
            Shows the remaining Consonant

            */

            case Constants.SYMBOL:
                kv.setKeyboard(symbols);
                updateDigits(language);
                currentKeyboard = Constants.CurrentKeyboard_SYMBOL;
                currentEventTriggered = Constants.SYMBOL;
                break;

            case Constants.PHONEPAD:
                kv.setKeyboard(phonepad);
                updateDigits(language);
                currentKeyboard = Constants.CurrentKeyboard_PHONEPAD;
                currentEventTriggered = Constants.PHONEPAD;
                break;

            case Constants.ABC:
                //ABC key Alpha Mode
                kv.setKeyboard(keyboard);
                currentKeyboard = Constants.CurrentKeyboard_MAIN;
                currentAdaptive=true; //start from beginning
                changeAdaptive(Constants.ABC); //Redraw
                kv.invalidateAllKeys();
                break;

            case Constants.main_to_extended_consonant:
                if (extendedKeyboard != keyboard) {
                    kv.setKeyboard(extendedKeyboard);
                    currentKeyboard = Constants.CurrentKeyboard_EXTENDED;
                    currentAdaptive=false; //retain the state
                    changeAdaptive(Constants.main_to_extended_consonant); //Redraw
                    kv.invalidateAllKeys();
                }
                break;

            /*
            Current Keyboard is Extended Consonants.
            Load Main Keyboard.
            Triggered by prev arrow.

             */

            case Constants.extended_consonant_to_main:
                kv.setKeyboard(keyboard);
                currentKeyboard = Constants.CurrentKeyboard_MAIN;
                currentAdaptive=false; //retain the state
                changeAdaptive(Constants.extended_consonant_to_main); //Redraw
                kv.invalidateAllKeys();
                break;

            /*
            Adaptive Area has Consonants right now.
            Load vowels in the Adaptive Area

             */
            case Constants.adaptive_consonantCombinations_to_vowel:
                currentViewHasVowel = true;
                currentAdaptive=true; //start from first
                changeAdaptive(Constants.adaptive_consonantCombinations_to_vowel);
                kv.invalidateAllKeys();
                break;
              /*
            Adaptive Area has Vowels right now.
            Load consonant combinations in the Adaptive Area .
             consonant is the current consonant pressed or the consonant last pressed.

             */
            case Constants.adaptive_vowel_to_consonantCombinations:

                currentViewHasVowel = false;
                currentAdaptive=true; //start from beginning
                changeAdaptive(Constants.adaptive_vowel_to_consonantCombinations);
                kv.invalidateAllKeys();
                break;

            case Constants.extended_adaptive:
                currentAdaptive = false;
                changeAdaptive(Constants.extended_adaptive);
                kv.invalidateAllKeys();
                break;

            case Constants.main_adaptive:
                currentAdaptive = true;
                changeAdaptive(Constants.main_adaptive);
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
                if (LanguageUtilites.IsConsonant(primaryCode, language)) {
                    last_consonant_pressed = primaryCode;
                    currentViewHasVowel = false;
                    currentAdaptive=true; //start from beginning
                    changeAdaptive(Constants.adaptive_consonantFillsCombinations);
                    kv.invalidateAllKeys();

                }

                    /*
                    Check if the char is a dependent vowel of the Script.


                     */
                if (LanguageUtilites.IsDependentVowel(primaryCode, language)) {
                    int pre = LanguageUtilites.IndependentPretext(primaryCode, language);
                    if (pre != -1 && currentViewHasVowel) {

                        ic.commitText(String.valueOf((char) pre), 1);

                    }
                    boolean dontaddprevious = currentEventTriggered == Constants.adaptive_consonantFillsCombinations;

                    if (!dontaddprevious && !currentViewHasVowel) {
                        ic.commitText(String.valueOf((char) last_consonant_pressed), 1); //Add previous coz Adaptive lies open
                    }
                    currentEventTriggered = Constants.adaptive_dependent;

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
        int[] dependentVowels;
        int[] independentVowels;

        currentEventTriggered = eventcode; //Set the Event

        first_consonant = (currentKeyboard == Constants.CurrentKeyboard_MAIN) ? LanguageUtilites.first(language) : LanguageUtilites.extendedFirst(language);
        if (!currentAdaptive) {   //Extended is needed
            dependentVowels = LanguageUtilites.getDependentVowelsExtended(language, displayMode);
            independentVowels = LanguageUtilites.getIndependentVowelsExtended(language, displayMode);
        } else {
            //In all remaining events, the basic
            dependentVowels = LanguageUtilites.getDependentVowels(language, displayMode);
            independentVowels = LanguageUtilites.getIndependentVowels(language, displayMode);
        }

        //Get all the Keys of the keyboard. No other option!
        ArrayList<Keyboard.Key> keys = (ArrayList<Keyboard.Key>) kv.getKeyboard().getKeys();

        //Loop through all the keys
        for (Keyboard.Key k : keys) {


            if (k.codes[0] == first_consonant) {
                //got all the top keys before the first consonant
                //And the key is not the first one
                break; //break the loop

            }
            //event : change vowels in adaptive to consonant
            //change the key
            else if (k.codes[0] == Constants.adaptive_vowel_to_consonantCombinations) {
                if (!currentViewHasVowel) {
                    k.codes[0] = Constants.adaptive_consonantCombinations_to_vowel; //switch
                    k.label = null;
                    k.icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.rafar);
                }
            }
            //event : change barkhadi in adaptive area to vowels

            else if (k.codes[0] == Constants.adaptive_consonantCombinations_to_vowel) {
                if (currentViewHasVowel) {
                    k.codes[0] = Constants.adaptive_vowel_to_consonantCombinations; //change the code of the key
                    k.icon = null; //remove the icon here
                    k.label = String.valueOf((char) independentVowels[0]); //set the new label
                }

            }
            //event : Forward Arrow pressed in the Adaptive Area

            else if (k.codes[0] == Constants.extended_adaptive) {
                if (!currentAdaptive) {
                    k.codes[0] = Constants.main_adaptive;
                    k.icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.sym_keyboard_prev);
                }
                //To DO
            } else if (k.codes[0] == Constants.main_adaptive) {
                if (currentAdaptive) {
                    k.codes[0] = Constants.extended_adaptive;
                    k.icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.sym_keyboard_next);
                }
            } else {

                if (eventcode == Constants.adaptive_consonantFillsCombinations || eventcode == Constants.adaptive_vowel_to_consonantCombinations ||
                        ((eventcode == Constants.main_to_extended_consonant || eventcode == Constants.main_adaptive || eventcode == Constants.extended_adaptive || eventcode == Constants.ABC || eventcode == Constants.extended_consonant_to_main || eventcode == Constants.orientation_retain_adaptive) && !currentViewHasVowel)) {
                    //These events will mean that you need barakhdi in the Adaptive Area.

                    k.codes[0] = dependentVowels[keys.indexOf(k)]; //Get the Dependent Vowels
                    k.label = String.valueOf((char) last_consonant_pressed); //Add based on the consonant
                    if (k.codes[0] != last_consonant_pressed) {
                        k.label = k.label + String.valueOf((char) k.codes[0]);
                    } else {
                        k.label = k.label + " ";
                    }

                } else if (eventcode == Constants.adaptive_consonantCombinations_to_vowel || ((eventcode == Constants.main_to_extended_consonant || eventcode == Constants.main_adaptive || eventcode == Constants.extended_consonant_to_main || eventcode == Constants.extended_adaptive) && currentViewHasVowel)) {
                    //This event means you need Vowel Combinations

                    k.codes[0] = independentVowels[keys.indexOf(k)];
                    //this if this independentVowel needs pretext
                    int t = LanguageUtilites.IndependentPretext(k.codes[0], language);
                    if (t != -1) {
                        //Add additional Devanagari AA
                        k.label = String.valueOf((char) t) + String.valueOf((char) k.codes[0]);
                    } else {
                        //Other keys. Simply create the label from the codes
                        k.label = String.valueOf((char) k.codes[0]) + " ";
                    }

                }

                if (k.codes[0] == 0) {
                    //Empty Keys
                    k.label = null;
                    k.icon = null;

                }
            }

        }

    }

    public void updateDigits(String language) {
        int startDigit = LanguageUtilites.firstDigit(language);
        ArrayList<Keyboard.Key> keys = (ArrayList<Keyboard.Key>) kv.getKeyboard().getKeys();

        for (Keyboard.Key k : keys) {
            if (k.codes[0] >= 0 && k.codes[0] <= 9) {
                //got the digit keys
                //update the key

                k.codes[0] += startDigit; //add to original code
                //update the keys labels

                k.label = String.valueOf((char) k.codes[0]) + " ";

            }
        }


    }


    /**
     * Generates the layout resource id for the keyboard view based on the displayMode and current language
     *
     * @return layout resource id of the keyboard view to be shown
     */
    public int getKeyboardViewResourceId(boolean e) {
        String file = e ? (language + "_extended_consonants") : language;
        //boolean e shows extended Keyboard

        file = file + displayMode;
        Log.d("layout", "filename = " + file);

        return getResources().getIdentifier(file, "xml",
                getPackageName());
    }

    /**
     * Gets the layout file resource id of the keyboard based on
     *
     * @param layoutFile layout of the keyboard whose resource id is to be returned
     * @return Resource id of the layout file of the keyboard to be shown
     * <p/>
     * IMP: These remain same for all languages : Hindi,Gujarati...
     */
    public int getResourceId(String layoutFile) {

        return  getResources().getIdentifier( layoutFile, "xml",  getPackageName());
    }


    /**
     * Detects the display config(landscape or portrait) and sets the displayMode accordingly
     */
    public void detectDisplayMode() {
        int dispMode = getResources().getConfiguration().orientation;
        displayMode= (dispMode==1)?"":"_land";
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
