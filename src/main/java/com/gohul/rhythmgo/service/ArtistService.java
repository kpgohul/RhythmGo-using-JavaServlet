package com.gohul.rhythmgo.service;

import com.gohul.rhythmgo.dao.ArtistDAO;
import com.gohul.rhythmgo.dto.response.ArtistResponse;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.exception.ValidationException;
import com.gohul.rhythmgo.model.Artist;

public class ArtistService {

    private static final ArtistDAO artistDAO = new ArtistDAO();

    public int addArtist(String name) throws Exception {

        if (name == null || name.isBlank())
            throw new ValidationException("Artist name required");

        return artistDAO.save(name);
    }

    public ArtistResponse getArtistInfo(int artistId) throws Exception {

        Artist artist = artistDAO.findById(artistId);

        if (artist == null) {
            throw new ResourceNotFoundException("Artist", "ID", String.valueOf(artistId));
        }

        return new ArtistResponse(artistId, artist.getName());
    }


    public void deleteArtist(int artistId) throws Exception {
        if(!artistDAO.existsById(artistId)){
            throw new ResourceNotFoundException("Artist", "ID", String.valueOf(artistId));
        }

        if(!artistDAO.delete(artistId)){
            throw new RuntimeException("Artist Deletion Failed");
        }
    }

}
