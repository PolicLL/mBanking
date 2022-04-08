package com.example.mbankingapp.frontend;

public class RegisterInputTest
{

     public boolean isInputCorrect(String input)
    {
        return (areAllCharsAlphanumerical(input)) && (isStringLengthCorrect(input));
    }

    public boolean isPINCorrect(String pin)
    {
        return ((pin.length() >= 4) && (pin.length() <= 6));
    }

    // also checks if al chars are alphanumerical
    private boolean areAllCharsAlphanumerical(String text)
    {
        if((text.length() < 1) || (text.length() > 30))
            return false;

        for(int i = 0; i < text.length(); ++i)
        {
            char tempChar = text.charAt(i);

            if(!isLetter(tempChar))
                return false;
        }

        return true;
    }

    private boolean isLetter(char sign)
    {

        if(((int)sign >= 65) && ((int)sign <= 90))
            return true;

        if(((int)sign >= 97) && ((int)sign <= 122))
            return true;

        return sign == ' ';
    }

    // checks if text is smaller than 30 characters
    private boolean isStringLengthCorrect(String surname)
    {
        return (surname.length() >= 1) && (surname.length() <= 30);
    }
}
