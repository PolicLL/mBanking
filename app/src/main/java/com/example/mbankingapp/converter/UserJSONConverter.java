package com.example.mbankingapp.converter;

import com.example.mbankingapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserJSONConverter
{
    private User user;

    public UserJSONConverter() {
        this.user = new User();
    }

    public User convertJsonObjectToUser(JSONObject jsonObj) throws JSONException
    {
        int userId = jsonObj.getInt("user_id");
        user.setId(userId);

        return user;
    }
}
