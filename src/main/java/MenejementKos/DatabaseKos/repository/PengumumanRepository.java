package MenejementKos.DatabaseKos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import MenejementKos.DatabaseKos.model.PengumumanAdmin;

public interface PengumumanRepository extends JpaRepository<PengumumanAdmin, Long> {
}

