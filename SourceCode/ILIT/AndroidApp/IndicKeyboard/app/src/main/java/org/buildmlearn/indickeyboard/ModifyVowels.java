package org.buildmlearn.indickeyboard;

public class ModifyVowels {

    public static int[] hindi_dependent = new int[]{0, 2366, 2367, 2368, 2369, 2370, Constants.adaptive_vowel_to_consonantCombinations, 2375, 2376, 2379, 2380, 2306, 2307, Constants.extended_adaptive};
    public static int[] hindi_independent=new int[]{2309, 2310, 2311, 2312, 2313, 2314, Constants.adaptive_consonantCombinations_to_vowel, 2319, 2320, 2323, 2324, 2306, 2307, Constants.extended_adaptive};
    public static int[] error=new int[]{-1};
    public static int[] getDependentVowels(String language,int last_consonant)
    {
        if(language=="hindi")
        {
            int [] temp= hindi_dependent;
            temp[0]=last_consonant;
            return temp;
        }

       return error;
    }

    public static int[] getIndependentVowels(String language)
    {

        if(language=="hindi")
        {
            return hindi_independent;
        }

        return error;

    }








}

