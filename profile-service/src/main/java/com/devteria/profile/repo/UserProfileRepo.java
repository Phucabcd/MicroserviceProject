package com.devteria.profile.repo;


import com.devteria.profile.entiry.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends Neo4jRepository<UserProfile, String> {

}
