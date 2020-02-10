package com.ipiecoles.java.eval.mdd050.controller;

import com.ipiecoles.java.eval.mdd050.model.Artist;
import com.ipiecoles.java.eval.mdd050.repository.ArtistRepository;
import com.ipiecoles.java.eval.mdd050.service.ArtistService;
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
    private ArtistService artistService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Artist> getArtistById(@PathVariable("id") Integer id) {
        return artistService.getArtistById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody Page<Artist> getAllArtists(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortProperty") String sortProperty,
            @RequestParam(value = "sortDirection") String sortDirection,
            @RequestParam(value = "name", required = false) String name
    ) {
        return artistService.getAllArtists(page, size, sortProperty, sortDirection, name);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Artist addArtist(@RequestBody Artist artist) {
        return artistService.addArtist(artist);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody Artist saveArtist(@PathVariable("id") Integer id, @RequestBody Artist artist) {
        return artistService.saveArtist(id, artist);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody void delArtist(@PathVariable("id") Integer id) {
        artistService.delArtist(id);
    }

}
