package com.quitto.server.infrastructure.db.LinuxUser.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import com.quitto.server.infrastructure.db.User.Entity.UserEntity;

@Entity
public class LinuxUserEntity {
    @Id
    private int uid;

    @Column(nullable =  false)
    private String name;

    @Column(nullable =  true)
    private String shell;

    @Column(nullable =  false)
    private String homeDir;

    @ManyToOne
    @JoinColumn(name = "gid")
    private GroupsEntity group;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public LinuxUserEntity() {
    }

    public LinuxUserEntity(int uid, String name, String shell, String homeDir) {
        this.uid = uid;
        this.name = name;
        this.shell = shell;
        this.homeDir = homeDir;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getShell() {
        return shell;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public GroupsEntity getGroups() {
        return this.group;
    }

    public void setGroups(GroupsEntity group) {
        this.group = group;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
