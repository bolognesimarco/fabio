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
public class SuperPost implements Serializable{

	private static final long serialVersionUID = 2187173156450702049L;

	@Id
	@GeneratedValue
	private int id;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="superpost")
	private List<Post> posts = new ArrayList<Post>();

	@Column
	private String nomeSuperPost;

	@ManyToOne(optional=false)
	@JoinColumn(name="topicforum", nullable=true, updatable=false)
	private TopicForum topicforum;
	
//	GETTERS & SETTERS*****************
	
	public List<Post> getPosts() {
		return posts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public String getNomeSuperPost() {
		return nomeSuperPost;
	}

	public void setNomeSuperPost(String nomeSuperPost) {
		this.nomeSuperPost = nomeSuperPost;
	}

	public TopicForum getTopicforum() {
		return topicforum;
	}

	public void setTopicforum(TopicForum topicforum) {
		this.topicforum = topicforum;
	}

	
	
}
