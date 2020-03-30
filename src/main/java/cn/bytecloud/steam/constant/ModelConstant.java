package cn.bytecloud.steam.constant;

import java.util.ArrayList;
import java.util.List;

public class ModelConstant {

    public static final String CREATE_TIME = "l_create_time";
    public static final String UPDATE_TIME = "l_update_time";
    public static final String CREATOR_ID = "s_creator_id";
    public static final String ID = "_id";

    /**
     * user table
     */
    public static final String T_USER = "t_user";
    public static final String USER_NAME = "s_name";
    public static final String USER_TYPE = "s_type";
    public static final String USER_USERNAME = "s_username";
    public static final String USER_PASSWORD = "s_password";
    public static final String USER_USER_FLAG = "b_user_flag";
    public static final String USER_ROLE_IDS = "a_role_ids";

    /**
     * news table
     */
    public static final String T_NEWS = "t_news";
    public static final String NEWS_TITLE = "s_title";
    public static final String NEWS_CONTENT = "s_content";
    public static final String NEWS_APTH = "s_path";

    /**
     * category table
     */
    public static final String T_CATEGORY = "t_category";
    public static final String CATEGORY_NAME = USER_NAME;
    public static final String CATEGORY_ABBREVIATION = "s_abbreviation";
    public static final String CATEGORY_SIGN_UP_TIME = "l_sign_up_time";
    public static final String CATEGORY_SIGN_UP_END_TIME = "l_sign_up_end_time";
    public static final String CATEGORY_STATUS = "s_status";
    public static final String CATEGORY_MAX_MEMBER = "i_max_member";
    public static final String CATEGORY_MIN_MEMBER = "i_min_member";
    public static final String CATEGORY_GROUP = "a_group";
    public static final String CATEGORY_AREA_IDS = "a_area_ids";
    public static final String CATEGORY_SATGES = "a_stages";
    public static final String CATEGORY_PARAM_SETTING = "a_param_setting";
    public static final String CATEGORY_TELEPHONE_TYPE = "s_telephone_type";

    //stage 数组
    public static final String CATEGORY_STAGE_TYPE = "i_stage_type";
    public static final String CATEGORY_STAGE_END_TIME = "l_stage_end_time";
    public static final String CATEGORY_STAGE_PPT_NUM = "i_stage_ppt_num";
    public static final String CATEGORY_STAGE_DOC_NUM = "i_stage_doc_num";
    public static final String CATEGORY_STAGE_VIDEO_NUM = "i_stage_video_num";
    public static final String CATEGORY_STAGE_IMAGE_NUM = "i_stage_image_num";


    /**
     * project table
     */
    public static final String T_PROJECT = "t_project";
    public static final String PROJECT_NAME = USER_NAME;
    public static final String PROJECT_NUMBER = "s_number";
    public static final String PROJECT_CATEGORY_ID = "s_category_id";
    public static final String PROJECT_COUNT = "i_count";
    public static final String PROJECT_desc = "s_desc";
    public static final String PROJECT_ADDRESS = "s_address";
    public static final String PROJECT_TEACHERS = "a_teachers";
    public static final String PROJECT_TELEPHONE = "s_telephone";
    public static final String PROJECT_MATERIALS = "a_materials";
    public static final String PROJECT_SUBMIT_STATUS = "a_submit_status";
    public static final String PROJECT_MEMBERS = "a_members";
    public static final String PROJECT_SUBMIT_TIME = "a_submit_time";
    public static final String PROJECT_DOC_SUBMIT_TIME = "a_doc_submit_time";
    public static final String PROJECT_PRIZE = "s_prize";
    public static final String PROJECT_SCHOOL_ID = "s_school_id";
    public static final String PROJECT_AREA_ID = "s_area_id";
    public static final String PROJECT_GROUP = "s_group";
    public static final String PROJECT_DOC_SUBMIT_FLAG = "b_doc_submit_flag";
    public static final String PROJECT_SUBMIT_FLAG = "b_submit_flag";
    public static final String PROJECT_STAGE = "s_stage";
    public static final String PROJECT_ZIP_FLAG = "b_zip_flag";
    public static final String PROJECT_ELIMINATE_FLAG = "b_eliminate_flag";
    public static final String PROJECT_SIGN_UP_TIME = "l_sign_up_time";

    //teacher
    public static final String PROJECT_TEACHER_LEVLE = "s_teacher_level";
    public static final String PROJECT_TEACHER_NAME = "s_teacher_name";
    public static final String PROJECT_TEACHER_GENDER = "s_teacher_gender";
    public static final String PROJECT_TEACHER_SUBJECT = "s_teacher_subject";
    public static final String PROJECT_TEACHER_TELEPHONE = "s_teacher_telephone";

    //material
    public static final String PROJECT_MATERIAL_STAGE_TYPE = "s_material_stage_type";
    public static final String PROJECT_MATERIAL_PPT_PATH = "s_material_ppt_path";
    public static final String PROJECT_MATERIAL_DOC_PATHS = "s_material_doc_paths";
    public static final String PROJECT_MATERIAL_VIDEO_PATH = "s_material_vidoe_path";
    public static final String PROJECT_MATERIAL_IMAGE_PATHS = "a_material_image_paths";
    public static final String PROJECT_MATERIAL_DOC_SUBMIT_TIME = "b_doc_submit_time";

    //member
    public static final String PROJECT_MEMBER_NAME = "s_member_name";
    public static final String PROJECT_MEMBER_TYPE = "s_member_type";
    public static final String PROJECT_MEMBER_IMAGE_PATH = "s_member_iamge_path";
    public static final String PROJECT_MEMBER_GENDER = "s_member_gender";
    public static final String PROJECT_MEMBER_BIRTHDAY = "s_member_birthday";
    public static final String PROJECT_MEMBER_ID_CARD = "s_member_id_card";
    public static final String PROJECT_MEMBER_PASSPORT = "s_member_passport";
    public static final String PROJECT_MEMBER_SCHOOL_ID = "s_member_school_id";
    public static final String PROJECT_MEMBER_GROUP = "s_member_group";
    public static final String PROJECT_MEMBER_GRADE = "s_member_grade";
    public static final String PROJECT_MEMBER_TELEPHONE = "s_member_telephone";
    public static final String PROJECT_MEMBER_EMAIL = "s_member_email";
    public static final String PROJECT_MEMBER_PARENT = "a_member_parent";
    public static final String PROJECT_MEMBER_AREA_ID = "s_member_area_id";
    public static final String PROJECT_MEMBER_ADDRESS = "s_member_address";


    /**
     * area table
     */
    public static final String T_AREAT = "t_area";
    public static final String AREA_NAME = USER_NAME;
    public static final String AREA_PID = "s_pid";

    /**
     * competitionMsg table
     */
    public static final String T_COMPETITION_MSG = "t_competition_msg";
    public static final String COMPETITION_TITLE = "s_title";
    public static final String COMPETITION_CONTENT = "s_content";
    public static final String COMPETITION_TYPE = "s_type";
    public static final String COMPETITION_PATH = "s_path";

    /**
     * rule table
     */
    public static final String T_RULE = "t_rule";
    public static final String RULE_TITLE = "s_title";
    public static final String RULE_CONTENT = "s_content";
    public static final String RULE_IMAGE_APTH = "s_image_path";
    public static final String RULE_PATH = "s_path";
    public static final String RULE_NUMBER = "i_number";

    /**
     * banner table
     */
    public static final String T_BANNER = "t_banner";
    public static final String BANNER_TITLE = "s_title";
    public static final String BANNER_TYPE = "i_type";
    public static final String BANNER_NUMBER = "i_number";
    public static final String BANNER_BIG_IMAGE_PAHT = "s_big_image_path";
    public static final String BANNER_SMALL_IMAGE_PAHT = "s_small_image_path";
    public static final String BANNER_URL = "s_url";

    /**
     * advert table
     */
    public static final String T_ADVERT = "t_advert";
    public static final String ADVERT_TITLE = "s_title";
    public static final String ADVERT_IMAGE_PATH = "s_image_path";
    public static final String ADVERT_URL = "s_url";
    public static final String ADVERT_TYPE = "s_type";


    /**
     * evnet table
     */
    public static final String T_EVENT = "t_event";
    public static final String EVENT_TITLE = "s_title";
    public static final String EVENT_STAGE = "s_stage";
    public static final String EVENT_START_TIME = "s_start_time";
    public static final String EVENT_END_TIME = "s_end_time";
    public static final String EVENT_ADDRESS = "s_address";

    /**
     * school table
     */
    public static final String T_SCHOOL = "t_school";
    public static final String SCHOOL_NAME = "s_name";
    public static final String SCHOOL_AREA_ID = "s_area_id";
    public static final String SCHOOL_GROUP = "i_group";


    /**
     * role table
     */
    public static final String T_ROLE = "t_role";
    public static final String ROLE_NAME = "s_name";
    public static final String ROLE_PERMISSIONS = "a_permissions";

    /**
     * menu table
     */
    public static final String T_MENU = "t_menu";
    public static final String MENU_NAME = "s_name";
    public static final String MENU_PERMISSION_IDS = "a_permission_ids";

    /**
     * permission table
     */
    public static final String T_PERMISSON = "t_permission";
    public static final String PERMISSON_NAME = "s_name";
    public static final String PERMISSON_INTERFACE_URL = "s_interface_url";

    /**
     * result table
     */
    public static final String T_RESULT = "t_result";
    public static final String RESULT_TITLE = "s_title";
    public static final String RESULT_PATH = "s_path";


    /**
     * video table
     */
    public static final String T_VIDEO = "t_video";
    public static final String VIDEO_PATH = "s_path";
    public static final String VIDEO_TITLE = "s_title";

    /**
     * sponsor table
     */
    public static final String T_SPONSOR = "t_sponsor";
    public static final String SPONSOR_PATH = "s_path";
    public static final String SPONSOR_url = "s_url";

    /**
     * export table
     */
    public static final String T_EXPORT = "t_export";
    public static final String EXPORT_NAME = "t_name";
    public static final String EXPORT_PATH = "t_path";
    public static final String EXPORT_STATUS = "s_status";
    public static final String EXPORT_DOWNLOAD_URL = "t_download_url";

    /**
     * stats table
     */
    public static final String T_STATS = "t_stats";
    public static final String STATS_USERNAME = "s_username";
    public static final String STATS_BID = "s_bid";
    public static final String STATS_TIME = "l_time";

}

