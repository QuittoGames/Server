package com.quitto.server.infrastructure.db.LinuxUser.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups")
public class GroupsEntity {

	@Id
	private int gid;

	@Column(nullable = false)
	private String name;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	public GroupsEntity() {
	}

	public GroupsEntity(int gid, String name, boolean isActive) {
		this.gid = gid;
		this.name = name;
		this.isActive = isActive;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

}
