/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.model;

import com.fptu.benchmarks.beans.Level;
import com.fptu.benchmarks.beans.Profile;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.context.Context;

/**
 *
 * @author sanh
 */
public class ProfileDetails {

    public static Profile profile;
    public static Level profileLevel;
    public static int totalItem;
    public static Context context;
    public static Map<String, String> metadataMap = new HashMap<>();

    public static int getTotalItem() {
        return totalItem;
    }

    public static void setTotalItem(int totalItem) {
        ProfileDetails.totalItem = totalItem;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ProfileDetails.context = context;
    }

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        ProfileDetails.profile = profile;
    }

    public static Level getProfileLevel() {
        return profileLevel;
    }

    public static void setProfileLevel(Level profileLevel) {
        ProfileDetails.profileLevel = profileLevel;
    }

    public static Map<String, String> getMetadataMap() {
        return metadataMap;
    }

    public static void setMetadataMap(Map<String, String> metadataMap) {
        ProfileDetails.metadataMap = metadataMap;
    }

    public ProfileDetails() {
    }

    public static void clearProfileDetails() {
        setMetadataMap(new HashMap<>());
        setProfile(null);
        setProfileLevel(null);
    }
}
