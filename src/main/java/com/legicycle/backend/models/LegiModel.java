package com.legicycle.backend.models;

import com.github.javafaker.Faker;

import java.util.ArrayList;

public class LegiModel {
//    {
//        "short_title": "Tax Cuts and Jobs Act",
    private String short_title;
//            "similarity_score": 86,
    private int similarity_score;
//            "congress": 115,
    private int congress;
//            "status": "ENACTED:SIGNED",
    private String status;
//            "bill_link": "https://www.congress.gov/bill/115th-congress/house-bill/1/text",
    private String bill_link;
//            "top_5_subjects": [
//        "American Samoa",
//                "Arctic and polar regions",
//                "Art, artists, authorship",
//                "Assault and harassment offenses",
//                "Aviation and airports"
//            ]
    private ArrayList<String> top_5_subjects = new ArrayList<>();
//    }

    public static LegiModel demoBuilder() {
        Faker fake = new Faker();
        LegiModel temp = new LegiModel();
        temp.setShort_title(fake.company().catchPhrase());
        temp.setSimilarity_score(fake.number().numberBetween(0, 100));
        temp.setCongress(fake.number().numberBetween(100, 130));
        temp.setStatus("ENACTED:SIGNED");
        temp.setBill_link(fake.internet().url());

        ArrayList<String> subs = new ArrayList<>();
        for (var i = 0; i++ < 5;) {
            subs.add(fake.company().buzzword());
        }
        temp.setTop_5_subjects(subs);

        return temp;
    }

    public LegiModel() {
    }

    public LegiModel(String short_title, int similarity_score, int congress, String status, String bill_link, ArrayList<String> top_5_subjects) {
        this.short_title = short_title;
        this.similarity_score = similarity_score;
        this.congress = congress;
        this.status = status;
        this.bill_link = bill_link;
        this.top_5_subjects = top_5_subjects;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public int getSimilarity_score() {
        return similarity_score;
    }

    public void setSimilarity_score(int similarity_score) {
        this.similarity_score = similarity_score;
    }

    public int getCongress() {
        return congress;
    }

    public void setCongress(int congress) {
        this.congress = congress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBill_link() {
        return bill_link;
    }

    public void setBill_link(String bill_link) {
        this.bill_link = bill_link;
    }

    public ArrayList<String> getTop_5_subjects() {
        return top_5_subjects;
    }

    public void setTop_5_subjects(ArrayList<String> top_5_subjects) {
        this.top_5_subjects = top_5_subjects;
    }
}
