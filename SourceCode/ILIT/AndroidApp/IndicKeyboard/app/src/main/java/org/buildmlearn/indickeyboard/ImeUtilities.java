package org.buildmlearn.indickeyboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Will have all the Utility methods for IME & Subtypes
 */
public class ImeUtilities {

    private static Map<String,String> languageMap=createLanguageMap();

    private static Map<String,String> createLanguageMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("hi_IN","hindi");
        map.put("gu_IN","gujarati");

        //Similarly will add all the languages
        return Collections.unmodifiableMap(map);
    }

    public static String getLanguage(String code)
    {
        String lang=languageMap.get(code);
        if(lang==null)
        {
            //Value not found
            lang="hindi"; //default
        }
        return lang;
    }


}
