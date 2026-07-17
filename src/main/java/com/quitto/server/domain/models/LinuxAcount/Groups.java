package com.quitto.server.domain.models.LinuxAcount;

public class Groups {
    private int GID;
    private String name;
    private boolean is_active;

    public Groups() {

    }

    public Groups(int GID, String name, boolean is_active) {
        this.GID = GID;
        this.name = name;
        this.is_active = is_active;
    }

    public int getGID() {
        return GID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        if (obj == this){
            return true;
        }

        Groups other = (Groups) obj;
        return this.GID > 0 && this.GID == other.GID;
    }

    @Override
    public String toString() {
        return "Groups [GID=" + GID + ", name=" + name + ", is_active=" + is_active + "]";
    }
}
