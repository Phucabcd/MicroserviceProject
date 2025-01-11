package com.devteria.profile.mapper;

import com.devteria.profile.dto.response.UserProfileResponse;
import org.mapstruct.Mapper;

import com.devteria.profile.dto.request.ProfileRequest;
import com.devteria.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileRequest request);

    UserProfileResponse toUserProfileReponse(UserProfile entity);
}
