package com.pratik.insta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	
	private int id;
	private String username;
	private String email;
	private String name;
	private String userImage;
}
