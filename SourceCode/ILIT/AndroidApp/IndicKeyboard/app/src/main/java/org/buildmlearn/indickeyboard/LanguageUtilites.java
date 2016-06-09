package org.buildmlearn.indickeyboard;

public class LanguageUtilites {
    private static final int hindi_firstConsonant = 2325;
    private static final int hindi_firstExtendedConsonant = 2358;
    private static int[] hindi_dependentVowels= new int[]{2381, 2366, 2367, 2368, 2369, 2370,2375, 2376, 2379, 2380, 2306, 2307,2305,2364,2373,2374,2377,2378,2402,2403 };
    private static int[] hindi_independentVowels = new int[]{2309, 2310, 2311, 2312, 2313, 2314,2319, 2320, 2323, 2324, 2306, 2307,2305,2308, 2365, 2315,2316,2318,2321,2322,2355,2356,2400,2401,2384 };
    private static final int gujarati_firstConsonant = 2709;
    private static final int gujarati_firstExtendedConsonant = 2742;
    private static int[] gujarati_dependentVowels= new int[]{2765,2750,2751,2752,2753,2754, 2759 ,2760, 2763, 2764, 2690, 2691,	2689, 2748, 2755,2756,	2757,	2761,2786,2786 };
    private static int[] gujarati_independentVowels = new int[]{2693, 2694, 2695, 2696,2697, 2698,2703, 2704, 2707,2708, 2690, 2691, 2749,2699,2700,2784,2785,2801,2768 };
    private static int[] error = new int[]{-1};


    public static int first(String language) {
        if (language == "hindi") {
            return hindi_firstConsonant;
        }

        else if (language == "gujarati") {
            return gujarati_firstConsonant;
        }


        return 0;
    }

    public static int extendedFirst(String language) {
        if (language == "hindi") {
            return hindi_firstExtendedConsonant;
        }
        //more languages will somehere
        else if (language == "gujarati") {
            return gujarati_firstExtendedConsonant;
        }
        return 0;
    }


    public static int[] getDependentVowels(String language,String displayMode) {
        if (language == "hindi") {
            return getAdaptiveArea(hindi_dependentVowels,Constants.adaptive_vowel_to_consonantCombinations,0,displayMode);
        }

        else if (language == "gujarati") {
            return getAdaptiveArea(gujarati_dependentVowels,Constants.adaptive_vowel_to_consonantCombinations,0,displayMode);
        }

        return error;
    }

    public static int[] getDependentVowelsExtended(String language,String displayMode) {
        if (language == "hindi") {
            return getAdaptiveArea(hindi_dependentVowels,Constants.adaptive_vowel_to_consonantCombinations,12,displayMode);
        }

        else if (language == "gujarati") {
            return getAdaptiveArea(gujarati_dependentVowels,Constants.adaptive_vowel_to_consonantCombinations,12,displayMode);
        }

        return error;
    }

    public static int[] getAdaptiveArea(int[] t,int mode,int startPos,String displayMode)
    {
        int temp[]=new int[14];//fixed always
        temp[12]=mode;
        temp[13]=Constants.extended_adaptive;
        int high=(startPos==0)?13:t.length-startPos;
        for(int i=0,j=startPos;i<high; i++)
        {
            if(displayMode=="" && i==6)
            {
                temp[i]=mode;
                temp[12]=0; //clearing it
            }
            else {
                temp[i] = t[j++];
            }

        }


        return temp;
    }


    public static int[] getIndependentVowels(String language,String displayMode) {

        if (language == "hindi") {
            return getAdaptiveArea(hindi_independentVowels,Constants.adaptive_consonantCombinations_to_vowel,0,displayMode);
        }


        else   if (language == "gujarati") {
            return getAdaptiveArea(gujarati_independentVowels,Constants.adaptive_consonantCombinations_to_vowel,0,displayMode);
        }

        return error;

    }

    public static int[] getIndependentVowelsExtended(String language,String displayMode) {

        if (language == "hindi") {
            return getAdaptiveArea(hindi_independentVowels,Constants.adaptive_consonantCombinations_to_vowel,12,displayMode);
        }


        else   if (language == "gujarati") {
            return getAdaptiveArea(gujarati_independentVowels,Constants.adaptive_consonantCombinations_to_vowel,12,displayMode);
        }

        return error;

    }



    /*
   Checks if the Key is Consonant of the Script or not
       */

    public static boolean IsConsonant(int code,String language)
    {
        //As per Devanagari IndicSyllabic UCD Documentation
        //0x915 to 0x939 range 1 Devanagari Consonant ...0x958 to 0x95F range 2 Devanagari Consonant
        if(language=="hindi") {
            return (code >= 2325 && code <= 2361) || (code >= 2393 && code <= 2399);
        }

        else if(language=="gujarati")
        {
            return (code>=2709 && code <= 2728) ||(code>=2730 && code <= 2736) ||(code==2738 ) || (code == 2739) ||  (code>=2741 && code <= 2745) || (code ==2809);
        }

        return false;


    }

    public static int IndependentPretext(int code,String language)
    {
        //This is usually required, when the letter is Independent Vowel, but requires pretext say

        int temp=-1;
        if(language=="hindi")
        {
            if(code==2305 || code==2306 ||code==2307)
            {
                temp= hindi_independentVowels[0] ;

            }
        }

        else if(language=="gujarati")
    {
        if(code == 2689 || code==2690 || code == 2691 )
        {
            temp=gujarati_independentVowels[0];
        }
    }
    return temp;
    }

    /*
    Just a simple utility function to see if part of array
     */
    public static boolean contains(int[] array, final int key) {
        for (int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }

    /*
    Checks if the key is dependent Vowel or not

     */

    public static boolean IsDependentVowel(int code,String language)
    {
        if(language=="hindi") {
            //As per Devanagari IndicSyllabic UCD Documentation
            return contains(hindi_dependentVowels,code);
        }

        else if(language=="gujarati")
        {
            return contains(gujarati_dependentVowels,code);
        }

        return false; //language not found

    }


}


