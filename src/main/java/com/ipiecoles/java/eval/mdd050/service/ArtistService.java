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

import java.lang.reflect.Field;
import java.util.Arrays;
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

    public Page<Artist> getAllArtists(int page, int size, String sortProperty, Sort.Direction sortDirection, String name) {



        if(Arrays.stream(Artist.class.getDeclaredFields()).
                map(Field::getName).
                filter(s -> s.equals(sortProperty)).count() != 1){
            throw new IncorrectParameterException("La propriété " + sortProperty + " n'existe pas !");
        };

        Pageable pageable = PageRequest.of(page, size, sortDirection, sortProperty);

        if (name == null) {
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

        if (artist.isNullName()) {
            throw new IncorrectParameterException("Can't save artist without name");
        }

        if (artist.isNullId()) {
            throw new IncorrectParameterException("ID of artist JSON object is empty or null");
        }

        if (!artist.getId().equals(artistFromDb.get().getId())) {
            throw new IncorrectParameterException("Ambiguous ID, ID parameter and artist ID of JSON object are different");
        }

        Artist newArtist = artistFromDb.get();
        newArtist.setName(artist.getName());

        return artistRepository.save(newArtist);
    }


    public void delArtist(Integer id) {

        Optional<Artist> artistFromDb = artistRepository.findById(id);

        if (!artistFromDb.isPresent()) {
            throw new NotFoundException("Artist not found");
        }

        artistRepository.delete(artistFromDb.get());

    }

}
