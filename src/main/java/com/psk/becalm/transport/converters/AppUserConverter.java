package com.psk.becalm.transport.converters;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.transport.dto.model.AppUserDto;


public class AppUserConverter extends DtoConverter<AppUser, AppUserDto> {

    @Override
    protected AppUser convertToEntity(AppUserDto appUserDto) {
        return AppUser.builder()
                .userRole(new Role(RoleUserEnum.valueOf(appUserDto.getRole())))
                .email(appUserDto.getEmail())
                .password(appUserDto.getPassword())
                .name(appUserDto.getName())
                .surname(appUserDto.getSurname())
                .build();
    }

    @Override
    protected AppUserDto convertToDto(AppUser appUser) {
        return AppUserDto.builder()
                .email(appUser.getEmail())
                .name(appUser.getName())
                .password(appUser.getPassword())
                .role(appUser.getUserRole().toString())
                .build();
    }


    public static AppUserDto toDto(AppUser appUser) {
        return new AppUserConverter().convertToDto(appUser);
    }

    public static AppUser toEntity(AppUserDto appUserDto) {
        return new AppUserConverter().convertToEntity(appUserDto);
    }

}





