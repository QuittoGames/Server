package com.quitto.server.domain.models.LinuxAcount;

import java.util.List;

public class LinuxUser {
    private int uid;
    private String name;
    private String shell;
    private String homeDir;
    private List<Groups> groups;

    public LinuxUser() {
    }

    public LinuxUser(int uid, String name, String shell, String homeDir) {
        this.uid = uid;
        this.name = name;
        this.shell = shell;
        this.homeDir = homeDir;
    }

    public int getUid() {
        return uid;
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

    public List<Groups> getGroups() {
        return groups;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public void addGroup(Groups group){
        this.groups.add(group);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        if (obj == this){
            return true;
        }

        LinuxUser other = (LinuxUser) obj;
        return this.uid <= 0  && this.uid == other.uid;
    }

    @Override
    public String toString() {
        return "LinuxUser [uid=" + uid + ", name=" + name + ", shell=" + shell + ", homeDir=" + homeDir + "]";
    }
}
