package com.ipiecoles.java.eval.mdd050.repository;


import com.ipiecoles.java.eval.mdd050.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Optional<Artist> findByName(String name);

    Page<Artist> findByName(Pageable pageable, String name);

}
