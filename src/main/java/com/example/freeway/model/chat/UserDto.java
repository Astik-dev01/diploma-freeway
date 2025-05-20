package com.example.freeway.model.chat;

import com.example.freeway.db.entity.SysUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String fullName;

    public static UserDto from(SysUser user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getSecondName() + " " + user.getName())
                .build();
    }
}
