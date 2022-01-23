package com.psk.becalm.transport.converters;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.transport.dto.model.AppUserDto;

import java.util.Set;


public class AppUserConverter extends DtoConverter<AppUser, AppUserDto> {

    @Override
    protected AppUser convertToEntity(AppUserDto appUserDto) {
        return AppUser.builder()
                .userRole(Set.of(new Role(RoleUserEnum.USER)))
                .username(appUserDto.getUsername())
                .email(appUserDto.getEmail())
                .password(appUserDto.getPassword())
                .firstName(appUserDto.getFirstName())
                .surname(appUserDto.getSurname())
                .build();
    }
    @Override
    protected AppUserDto convertToDto(AppUser appUser) {
        return AppUserDto.builder()
                .firstName(appUser.getFirstName())
                .email(appUser.getEmail())
                .surname(appUser.getSurname())
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .build();
    }
    public static AppUserDto toDto(AppUser appUser) {
        return new AppUserConverter().convertToDto(appUser);
    }

    public static AppUser toEntity(AppUserDto appUserDto) {
        return new AppUserConverter().convertToEntity(appUserDto);
    }

}





