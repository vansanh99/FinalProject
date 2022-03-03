/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.beans;

/**
 *
 * @author sanh
 */
public class ProfileDetails {
    public static Profile profile;
    public static String profileLevel;
    public static String name;

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        ProfileDetails.profile = profile;
    }

    public static String getProfileLevel() {
        return profileLevel;
    }

    public static void setProfileLevel(String profileLevel) {
        ProfileDetails.profileLevel = profileLevel;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ProfileDetails.name = name;
    }

    public ProfileDetails() {
    }
}
