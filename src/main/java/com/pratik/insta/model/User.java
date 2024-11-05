package com.pratik.insta.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pratik.insta.dto.UserDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String username;

	private String name;

	private String password;

	private String email;
	private String mobile;
	private String website;
	private String bio;
	private String gender;
	private String image;

	// The reason to choose set interface that it will not contain any duplicate
	// follower and following
	@Embedded
	@ElementCollection
	private Set<UserDto> follower = new HashSet<UserDto>();
	
	@Embedded
	@ElementCollection
	private Set<UserDto> following = new HashSet<UserDto>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Story> stories = new ArrayList<>();

	@ManyToMany // Many Users can save many posts
	private List<Post> savedPost = new ArrayList<>();

}
