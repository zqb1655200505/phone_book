package com.zqb.javaBean;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zqb on 2016/10/24.
 */
public class User {
    private int id;//可能为用户id,也可能为号码id;
    private String name;
    private String phone_number;
    private Map<String, String> errors = new HashMap<String, String>();

    public Map<String, String> getErrors() {
           return errors;
     }
     public void setErrors(Map<String, String> errors) {
         this.errors = errors;
     }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    public boolean validate()
    {
        if(this.name.length()>10||this.name.length()<=0)
        {
            errors.put("code","error");
            errors.put("msg","用户名不符");
            return false;
        }
        if(!isNumeric(this.phone_number)||this.phone_number.length()>11)
        {
            errors.put("code","error");
            errors.put("msg","电话号码不符");
            return false;
        }
        errors.put("code","success");
        return true;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
