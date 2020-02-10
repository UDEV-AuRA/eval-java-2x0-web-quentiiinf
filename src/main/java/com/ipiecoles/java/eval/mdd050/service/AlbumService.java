package com.ipiecoles.java.eval.mdd050.service;

import com.ipiecoles.java.eval.mdd050.model.Album;
import com.ipiecoles.java.eval.mdd050.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    AlbumRepository albumRepository;

    public void delAlbum(Integer id)
    {
        Optional<Album> album = albumRepository.findById(id);

        if (!album.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }

        albumRepository.delete(album.get());
    }


    public Album addAlbum(Album album)
    {
        return albumRepository.save(album);
    }

}
