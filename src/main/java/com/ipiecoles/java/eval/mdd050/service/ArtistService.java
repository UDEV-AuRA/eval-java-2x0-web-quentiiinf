package com.ipiecoles.java.eval.mdd050.service;

import com.ipiecoles.java.eval.mdd050.exceptions.ConflictException;
import com.ipiecoles.java.eval.mdd050.exceptions.IncorrectParameterException;
import com.ipiecoles.java.eval.mdd050.exceptions.NotFoundException;
import com.ipiecoles.java.eval.mdd050.exceptions.NullPropertyException;
import com.ipiecoles.java.eval.mdd050.model.Artist;
import com.ipiecoles.java.eval.mdd050.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ArtistService
{

    @Autowired
    private ArtistRepository artistRepository;

    public Optional<Artist> getArtistById(Integer id) {

        Optional<Artist> artist = artistRepository.findById(id);

        if (artist.isPresent()) {
            return artistRepository.findById(id);
        }

        throw new NotFoundException("Artist not found");
    }

    public Page<Artist> getAllArtists(int page, int size, String sortProperty, String sortDirection, String name) {

        Pageable pageable;

        if (sortDirection.equalsIgnoreCase("ASC")) {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).ascending());
        } else if (sortDirection.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).descending());
        } else {
            throw new IncorrectParameterException("Parameter SortDirection must have a value of ASC or DESC");
        }

        if(name == null) {
            return artistRepository.findAll(pageable);
        }

        return artistRepository.findByName(pageable, name);
    }

    public Artist addArtist(Artist artist) {

        if (artist.isNullName() || artist.getName().isEmpty()) {
            throw new NullPropertyException("Can't add an album without name");
        }

        Optional<Artist> artistsPresent = artistRepository.findByName(artist.getName());

        if (artistsPresent.isPresent()) {
            throw new ConflictException("Artist with this name already exists");
        }

        return artistRepository.save(artist);
    }


    public Artist saveArtist(Integer id, Artist artist) {

        Optional<Artist> artistsPresent = artistRepository.findByName(artist.getName());

        if (artistsPresent.isPresent()) {
            throw new ConflictException("Artist with this name already exists");
        }

        Optional<Artist> artistFromDb = artistRepository.findById(id);

        if (!artistFromDb.isPresent()) {
            throw new NotFoundException("Artist not found");
        }

        artist.setId(id);

        return artistRepository.save(artist);
    }


    public void delArtist(Integer id) {

        Optional<Artist> artistFromDb = artistRepository.findById(id);

        if (!artistFromDb.isPresent()) {
            throw new NotFoundException("Artist not found");
        }

        artistRepository.delete(artistFromDb.get());

    }

}
