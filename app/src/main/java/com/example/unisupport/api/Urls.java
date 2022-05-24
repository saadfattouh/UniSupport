package com.example.unisupport.api;

public class Urls {

    public static final String IP_ADDRESS = "192.168.0.107";
    public static final String PUBLIC_FOLDER = "http://" + IP_ADDRESS + "/unisupport/v1/public/";
    public static final String BASE_URL = "http://" + IP_ADDRESS + "/unisupport/v1/public/api/";

    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String UPDATE_PROFILE = "update-profile";

    public static final String ASK_EMPLOYEES = "add_administrative_consultation";
    public static final String GET_STUDENT_EMPLOYEES_QUESTIONS = "get_administrative_consultation";

    public static final String GET_ALL_PSYCHOLOGISTS = "get_all_psychologists";
    public static final String ASK_PSYCHOLOGIST = "add_psychologist_consultation";
    public static final String GET_STUDENT_PSYCHOLOGISTS_QUESTIONS = "get_psychologist_consultations";

    public static final String GET_USER_CHATS = "get_user_chats";
    public static final String SEND_CHAT_MESSAGE = "send_message";
    public static final String GET_CHAT_MESSAGES = "get_chat_messages";


    public static final String GET_ALL_ADMINISTRATIVE_STUDENTS_CONSULTATIONS = "get_all_administrative_consultations";
    public static final String EMPLOYEE_REPLY_TO_STUDENTS_CONSULTATIONS = "replay_to_administrative_consultation";

    public static final String GET_ALL_PSYCHOLOGIST_STUDENTS_CONSULTATIONS = "get_psychologist_consultations";
    public static final String PSYCHOLOGIST_REPLY_TO_STUDENTS_CONSULTATIONS = "replay_to_psychologist_consultation";
    public static final String EVALUATE_PSYCHOLOGIST = "update_psychologist_evaluation";

    public static final String ADD_NEW_POST = "add_post";
    public static final String UPDATE_USER_REACTION = "update_user_reaction";
    public static final String GET_ALL_POSTS = "get_all_posts";
    public static final String GET_POST_TOOLS = "get_post_tools";
    public static final String ADD_TOOL = "add_tool";
    public static final String GET_POST_REFERENCES = "get_post_references";
    public static final String GET_POST_REACTIONS = "get_post_reactions";
    public static final String ADD_REFERENCE = "add_reference";
    public static final String GET_PUBLIC_CHAT_MESSAGES = "get_public_chat";
    public static final String ADD_NEW_PAGE = "add_page";
    public static final String GET_ALL_PAGES = "get_all_pages";
    public static final String GET_ALL_STUDENTS = "get_all_students";
}
