package org.buildmlearn.indickeyboard;

public class LanguageUtilites {
    private static final int hindi_firstConsonant = 2325;
    private static final int hindi_firstExtendedConsonant = 2358;
    private static int[] hindi_dependentVowels= new int[]{0, 2366, 2367, 2368, 2369, 2370, Constants.adaptive_vowel_to_consonantCombinations, 2375, 2376, 2379, 2380, 2306, 2307, Constants.extended_adaptive};
    private static int[] hindi_dependentVowels_land=new int[]{0,2366,2367,2368,2369,2370,2375,2376, 2379, 2380, 2306, 2307,Constants.adaptive_vowel_to_consonantCombinations,Constants.extended_adaptive};
    private static int[] hindi_independentVowels = new int[]{2309, 2310, 2311, 2312, 2313, 2314, Constants.adaptive_consonantCombinations_to_vowel, 2319, 2320, 2323, 2324, 2306, 2307, Constants.extended_adaptive};
    private static int[] hindi_independentVowels_land=new int[]{2309, 2310, 2311, 2312, 2313, 2314, 2319, 2320, 2323, 2324, 2306, 2307, Constants.adaptive_consonantCombinations_to_vowel,Constants.extended_adaptive};
    private static int[] error = new int[]{-1};


    public static int first(String language) {
        if (language == "hindi") {
            return hindi_firstConsonant;
        }


        return 0;
    }

    public static int extendedFirst(String language) {
        if (language == "hindi") {
            return hindi_firstExtendedConsonant;
        }
        //more languages will somehere

        return 0;
    }


    public static int[] getDependentVowels(String language, int last_consonant,String displayMode) {
        if (language == "hindi") {
            int[] temp =   (displayMode=="")?hindi_dependentVowels:hindi_dependentVowels_land;
            temp[0] = last_consonant;
            return temp;
        }

        return error;
    }

    public static int[] getIndependentVowels(String language,String displayMode) {

        if (language == "hindi") {
            return (displayMode=="")?hindi_independentVowels:hindi_independentVowels_land;
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

        return false;


    }

    /*
    Checks if the key is dependent Vowel or not

     */

    public static boolean IsDependentVowel(int code,String language)
    {
        if(language=="hindi") {
            //As per Devanagari IndicSyllabic UCD Documentation
            return (code == 2306) || (code == 2307) || (code == 2362) || code == 2363 || (code >= 2366 && code <= 2383) || (code >= 2389 && code <= 2391) || (code == 2402) || (code == 2403);
        }

        return false;

    }


}

