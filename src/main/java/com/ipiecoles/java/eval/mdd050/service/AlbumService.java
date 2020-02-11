package com.ipiecoles.java.eval.mdd050.service;

import com.ipiecoles.java.eval.mdd050.exceptions.NotFoundException;
import com.ipiecoles.java.eval.mdd050.exceptions.NullPropertyException;
import com.ipiecoles.java.eval.mdd050.model.Album;
import com.ipiecoles.java.eval.mdd050.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public void delAlbum(Integer id) {
        Optional<Album> album = albumRepository.findById(id);

        if (!album.isPresent()) {
            throw new NotFoundException("Album not found");
        }

        albumRepository.delete(album.get());
    }

    public Album addAlbum(Album album) {

        if (album.isNullTitle() || album.getTitle().isEmpty()) {
            throw new NullPropertyException("Can't add album without title");
        }

        return albumRepository.save(album);
    }

}
