package com.legicycle.backend.models.awsrds;

import javax.persistence.*;

@Entity
@Table(name = "testmodel")
public class TestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long uid;
    private String name;

    public TestModel() {
    }

    public TestModel(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
