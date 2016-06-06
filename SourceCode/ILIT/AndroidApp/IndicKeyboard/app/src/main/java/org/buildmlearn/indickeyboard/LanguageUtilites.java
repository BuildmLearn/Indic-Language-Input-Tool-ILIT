package org.buildmlearn.indickeyboard;

public class LanguageUtilites {
    private static final int hindi_firstConsonant = 2325;
    private static final int hindi_firstExtendedConsonant = 2358;
    private static int[] hindi_dependentVowels= new int[]{2381, 2366, 2367, 2368, 2369, 2370, Constants.adaptive_vowel_to_consonantCombinations, 2375, 2376, 2379, 2380, 2306, 2307, Constants.extended_adaptive};
    private static int[] hindi_dependentVowels_land=new int[]{2381,2366,2367,2368,2369,2370,2375,2376, 2379, 2380, 2306, 2307,Constants.adaptive_vowel_to_consonantCombinations,Constants.extended_adaptive};
    private static int[] hindi_independentVowels = new int[]{2309, 2310, 2311, 2312, 2313, 2314, Constants.adaptive_consonantCombinations_to_vowel, 2319, 2320, 2323, 2324, 2306, 2307, Constants.extended_adaptive};
    private static int[] hindi_independentVowels_land=new int[]{2309, 2310, 2311, 2312, 2313, 2314, 2319, 2320, 2323, 2324, 2306, 2307, Constants.adaptive_consonantCombinations_to_vowel,Constants.extended_adaptive};
    private static final int gujarati_firstConsonant = 2709;
    private static final int gujarati_firstExtendedConsonant = 2742;
    private static int[] gujarati_dependentVowels= new int[]{2765,2750,2751,2752,2753,2754, Constants.adaptive_vowel_to_consonantCombinations, 2759 ,2760, 2763, 2764, 2690, 2691, Constants.extended_adaptive};
    private static int[] gujarati_dependentVowels_land=new int[]{2765,2750,2751,2752,2753, 2754, 2759 ,2760, 2763, 2764, 2690, 2691,Constants.adaptive_vowel_to_consonantCombinations,Constants.extended_adaptive};
    private static int[] gujarati_independentVowels = new int[]{2693, 2694, 2695, 2696,2697, 2698, Constants.adaptive_consonantCombinations_to_vowel, 2703, 2704, 2707,2708, 2690, 2691, Constants.extended_adaptive};
    private static int[] gujarati_independentVowels_land=new int[]{2693, 2694, 2695, 2696,2697, 2698, 2703, 2704, 2707,2708, 2690, 2691, Constants.adaptive_consonantCombinations_to_vowel,Constants.extended_adaptive};
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
            int[] temp =   (displayMode=="")?hindi_dependentVowels:hindi_dependentVowels_land;
            return temp;
        }

        else if (language == "gujarati") {
            int[] temp =   (displayMode=="")?gujarati_dependentVowels:gujarati_dependentVowels_land;
            return temp;
        }


        return error;
    }

    public static int[] getIndependentVowels(String language,String displayMode) {

        if (language == "hindi") {
            return (displayMode=="")?hindi_independentVowels:hindi_independentVowels_land;
        }


        else   if (language == "gujarati") {
            return (displayMode=="")?gujarati_independentVowels:gujarati_independentVowels_land;
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
            if(code == 2306 || code == 2307)
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
    Checks if the key is dependent Vowel or not

     */

    public static boolean IsDependentVowel(int code,String language)
    {
        if(language=="hindi") {
            //As per Devanagari IndicSyllabic UCD Documentation
            return (code == 2306) || (code == 2307) || (code == 2362) || code == 2363 || (code >= 2366 && code <= 2383) || (code >= 2389 && code <= 2391) || (code == 2402) || (code == 2403);
        }

        else if(language=="gujarati")
        {
            return   (code>=2689 && code <=2691 ) || (code>=2750 && code <=2765 ) || (code>=2750 && code <=2765 ) || code==2786 ||  code==2787;
        }

        return false;

    }


}

