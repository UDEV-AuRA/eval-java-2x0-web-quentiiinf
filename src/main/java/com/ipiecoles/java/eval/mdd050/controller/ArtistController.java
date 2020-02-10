package com.ipiecoles.java.eval.mdd050.controller;

import com.ipiecoles.java.eval.mdd050.model.Artist;
import com.ipiecoles.java.eval.mdd050.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Artist> getArtistById(@PathVariable("id") Integer id) {

        Optional<Artist> artist = artistRepository.findById(id);

        if (artist.isPresent()) {
            return artistRepository.findById(id);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody Page<Artist> getAllArtists(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortProperty") String sortProperty,
            @RequestParam(value = "sortDirection") String sortDirection,
            @RequestParam(value = "name", required = false) String name
    ) {

        Pageable pageable;

        if (sortDirection.equalsIgnoreCase("ASC")) {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).ascending());
        } else if (sortDirection.equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).ascending());
        } else {
            throw new RuntimeException("Parameter SortDirection must have a value of ASC or DESC");
        }


        if(name == null) {
            return artistRepository.findAll(pageable);
        }

        return artistRepository.findByName(pageable, name);


    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Artist addArtist(@RequestBody Artist artist) {


        Optional<Artist> artistsPresent = artistRepository.findByName(artist.getName());

        if (artistsPresent.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Artist with this name already exists");
        }

        return artistRepository.save(artist);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody Artist saveArtist(@PathVariable("id") Integer id, @RequestBody Artist artist) {

        Optional<Artist> artistsPresent = artistRepository.findByName(artist.getName());

        if (artistsPresent.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Artist with this name already exists");
        }

        Optional<Artist> artistFromDb = artistRepository.findById(id);

        if (!artistFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }

        artist.setId(id);

        return artistRepository.save(artist);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody void delArtist(@PathVariable("id") Integer id) {

        Optional<Artist> artistFromDb = artistRepository.findById(id);

        if (!artistFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }

        artistRepository.delete(artistFromDb.get());


    }

}
