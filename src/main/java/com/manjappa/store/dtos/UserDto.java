package com.manjappa.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    //@JsonIgnore //To ignore field
    //@JsonProperty("user_id") //Renaming field adn sends to API client response
    private Long id;
    private String name;
    private String email;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //private LocalDateTime createdAt; //To provide some value will map at UserMapper class for test

    //@JsonInclude(JsonInclude.Include.NON_NULL) //Ignore null fields
    //private String phoneNumber; //User doesn't have phoneNumer, response will be sent as null


}
