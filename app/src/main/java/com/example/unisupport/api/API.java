package com.example.unisupport.api;

import com.example.unisupport.model.AdministrativeReply;
import com.example.unisupport.model.BaseMessage;
import com.example.unisupport.model.Chat;
import com.example.unisupport.model.Page;
import com.example.unisupport.model.Post;
import com.example.unisupport.model.PsychologistReply;
import com.example.unisupport.model.Reaction;
import com.example.unisupport.model.Reference;
import com.example.unisupport.model.Tool;

import java.io.File;
import java.util.ArrayList;

public interface API {

    //database :
    //users table : (int id, String first_name, String last_name, String email, string password, String phone, int type, String bio, String license_photo, String profile_photo, String job_career)

    //requests :
    void update_profile(int user_id, String f_name, String l_name, String phone, String bio, String profile_photo);

    Chat get_user_chats(int user_id); //returns the other person (id, name, image)
    BaseMessage get_chat_messages(int my_id, int his_id, boolean is_private);
    void send_message(int sender_id, int receiver_id, boolean is_private, String message);

    //for user
    void get_all_psychologists(); //returns only (id, name) of the psychologist
    void add_psychologist_consultation(int user_id, int psychologist_id, String content);
    PsychologistReply get_psychologist_consultations(int user_id, int psychologist_id); //returns also the psychologist_name
    void update_psychologist_evaluation(int user_id, int consultation_id, float rating); //is also update the psychologist average evaluation

    //for psychologist
    PsychologistReply get_psychologist_consultation_for_psychologist(int psychologist_id);//returns also the asker_name
    void reply_to_psychologist_consultation(int consultation_id, String reply);

    //for user
    void add_administrative_consultation(int user_id, String content);
    AdministrativeReply get_administrative_consultations(int user_id);//returns also the employee_name

    //for employee
    AdministrativeReply get_all_administrative_consultations();
    void reply_to_administrative_consultation(int consultation_id, int employee_id, String reply);


    //for user
    ArrayList<Post> getAllPosts();
    ArrayList<Page> getAllPages();
    void addPost(int userId, String title, String specialization, String description, Tool tool, File reference);
    void addPage(Page page);
    void updateUserReaction(int postId, int Reaction);
    void addReference(int postId, Reference reference);
    void addTool(int postId, Tool tool);

    ArrayList<Reference> getPostReferences(int post_id);
    ArrayList<Tool> getPostTools(int post_id);
    ArrayList<Integer> getPostReactions(int post_id);

}
