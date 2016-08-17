package org.buildmlearn.indickeyboard.prediction;

/**
 * Created by bhavita on 18/8/16.
 */
public class DBLanguage {

    private static String[] predictionLanguages ={ "hindi"}; //Add others here

    public static boolean hasPrediction(String lang)
    {
        boolean result=false;

        for(String i:predictionLanguages)
        {
            if(i==lang)
            {

        result=true;
                break;

            }
        }
     return result;
    }


}
