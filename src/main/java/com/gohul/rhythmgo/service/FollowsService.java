package com.gohul.rhythmgo.service;

import com.gohul.rhythmgo.dao.ArtistDAO;
import com.gohul.rhythmgo.dao.FollowsDAO;
import com.gohul.rhythmgo.dto.response.ArtistResponse;
import com.gohul.rhythmgo.exception.ResourceAlreadyExistException;
import com.gohul.rhythmgo.exception.ResourceNotFoundException;
import com.gohul.rhythmgo.model.Artist;

import java.util.List;

public class FollowsService {

    private final FollowsDAO followsDAO = new FollowsDAO();
    private final ArtistDAO artistDAO = new ArtistDAO();

    public void follow(int userId, int artistId) throws Exception {


        if (!artistDAO.existsById(artistId)) {
            throw new ResourceNotFoundException("Artist", "ID", String.valueOf(artistId));
        }
        if(followsDAO.isAlreadyFollowing(userId, artistId)){
            throw new ResourceAlreadyExistException("Following", "UserId & ArtistId", userId + " & "+artistId);
        }
        try {
            followsDAO.follow(userId, artistId);
        } catch (Exception e) {
            throw new Exception("User ID: "+userId+" failed to follow the artist ID: "+artistId+". Reason: "+e.getMessage());
        }
    }

    public void unfollow(int userId, int artistId) throws Exception {


        if (!artistDAO.existsById(artistId)) {
            throw new ResourceNotFoundException("Artist", "ID", String.valueOf(artistId));
        }
        if(!followsDAO.isAlreadyFollowing(userId, artistId)){
            throw new ResourceNotFoundException("Following", "UserId & ArtistId", userId + " & "+artistId);
        }
        try{
            boolean removed = followsDAO.unfollow(userId, artistId);
            if(!removed) throw new RuntimeException("Unfollow failed!");

        }catch (Exception e){
            throw new Exception("User ID: "+userId+" failed to unfollow the artist ID: "+artistId+". Reason: "+e.getMessage());
        }

    }

    public List<ArtistResponse> getAllFollowed(int userId) throws Exception {
        return followsDAO.getFollowedArtists(userId).stream()
                .map(artist -> new ArtistResponse(artist.getId(), artist.getName()))
                .toList();
    }
}
