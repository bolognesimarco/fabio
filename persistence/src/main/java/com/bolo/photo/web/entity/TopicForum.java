package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TopicForum implements Serializable{

	private static final long serialVersionUID = -3323143229739118696L;

	@Id
	@GeneratedValue
	private int id;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="topicforum")
	private List<SuperPost> superPosts = new ArrayList<SuperPost>();

	@Column
	private String nomeTopicForum;

	
	
//	GETTERS & SETTERS*****************
	

	public List<SuperPost> getSuperPosts() {
		return superPosts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSuperPosts(List<SuperPost> superPosts) {
		this.superPosts = superPosts;
	}

	public String getNomeTopicForum() {
		return nomeTopicForum;
	}

	public void setNomeTopicForum(String nomeTopicForum) {
		this.nomeTopicForum = nomeTopicForum;
	}
	
}
