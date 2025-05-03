package MenejementKos.DatabaseKos.Service;

import MenejementKos.DatabaseKos.model.Profile;
import MenejementKos.DatabaseKos.repository.ProfileRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile updatePaymentStatus(Long id, String status) {
        Profile profile = profileRepository.findById(id).orElseThrow();
        profile.setPaymentStatus(status);
        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }
}