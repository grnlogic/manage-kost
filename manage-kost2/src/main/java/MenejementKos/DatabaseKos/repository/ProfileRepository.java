package MenejementKos.DatabaseKos.repository;



import MenejementKos.DatabaseKos.model.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

import MenejementKos.DatabaseKos.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
