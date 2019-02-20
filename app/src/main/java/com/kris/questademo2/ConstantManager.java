package com.kris.questademo2;
import java.util.ArrayList;
import java.util.HashMap;
class ConstantManager {
    public static final String CHECK_BOX_CHECKED_TRUE = "YES";
    public static final String CHECK_BOX_CHECKED_FALSE = "NO";

    public static final String IS_LTQ_TRUE = "true";
    public static final String IS_LTQ_FALSE = "false";


    public static ArrayList<ArrayList<HashMap<String,String>>> childItems = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> parentItems = new ArrayList<>();


    public class Parameter{
        public static final String IS_CHECKED = "is_checked";
        public static final String SUB_CATEGORY_NAME = "sub_category_name";
        public static final String CATEGORY_NAME = "category_name";
        public static final String CATEGORY_INFO = "category_info";
        public static final String CATEGORY_ID = "category_id";
        public static final String CATEGORY_ISLTQ = "category_isltq";
        public static final String SUB_ID = "sub_id";

    }

}
