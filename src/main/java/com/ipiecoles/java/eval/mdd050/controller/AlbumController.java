package com.ipiecoles.java.eval.mdd050.controller;

import com.ipiecoles.java.eval.mdd050.model.Album;
import com.ipiecoles.java.eval.mdd050.model.Artist;
import com.ipiecoles.java.eval.mdd050.repository.AlbumRepository;
import com.ipiecoles.java.eval.mdd050.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody void delAlbum(@PathVariable("id") Integer id) {

        Optional<Album> album = albumRepository.findById(id);

        if (!album.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }

        albumRepository.delete(album.get());


    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Album addAlbum(@RequestBody Album album) {

        return albumRepository.save(album);

    }
}
