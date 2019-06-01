package com.anime.spagreen.utils;

import com.anime.spagreen.R;

public class ApiResources {


    String URL = MyAppClass.getContext().getString(R.string.api_url);

    String API_SECRECT_KEY = "api_secret_key="+MyAppClass.getContext().getString(R.string.api_key);

    String slider = URL+"get_slider?"+API_SECRECT_KEY;
    String latest_movie = URL+"get_latest_movies?"+API_SECRECT_KEY;
    String get_movie = URL+"get_movies?"+API_SECRECT_KEY+"&&page=";
    String get_featured_tv = URL+"get_featured_tv_channel?"+API_SECRECT_KEY;
    String get_live_tv = URL+"get_featured_tv_channel?"+API_SECRECT_KEY+"&&page=";

    String latestTvSeries = URL+"get_latest_tvseries?"+API_SECRECT_KEY;

    String tvSeries = URL+"get_tvseries?"+API_SECRECT_KEY+"&&page=";


    String allCountry = URL+"get_all_country?"+API_SECRECT_KEY;

    String allGenre = URL+"get_all_genre?"+API_SECRECT_KEY;

    String details = URL+"get_single_details?"+API_SECRECT_KEY;

    String movieByCountry = URL+"get_movie_by_country_id?"+API_SECRECT_KEY;
    String movieByGenre = URL+"get_movie_by_genre_id?"+API_SECRECT_KEY;
    String login = URL+"login?"+API_SECRECT_KEY;
    String signup = URL+"signup?"+API_SECRECT_KEY;

    String searchUrl = URL+"search?"+API_SECRECT_KEY;
    String favoriteUrl = URL+"get_favorite?"+API_SECRECT_KEY;

    String passResetUrl = URL+"password_reset?"+API_SECRECT_KEY+"&&email=";
    String profileUpdateURL = URL+"update_profile?"+API_SECRECT_KEY;
    String profileURL = URL+"get_user_details_by_email?"+API_SECRECT_KEY+"&&email=";
    String addFav = URL+"add_favorite?"+API_SECRECT_KEY;

    String favStatusURl = URL+"verify_favorite_list?"+API_SECRECT_KEY;
    String removeFav = URL+"remove_favorite?"+API_SECRECT_KEY;

    String addComment = URL+"add_comments?"+API_SECRECT_KEY;

    String commentsURL = URL+"get_all_comments?"+API_SECRECT_KEY;

    String addReplyURL = URL+"add_replay?"+API_SECRECT_KEY;
    String getAllReply = URL+"get_all_replay?"+API_SECRECT_KEY;
    String termsURL = MyAppClass.getContext().getString(R.string.terms_url);
    String genreMovieURL = URL+"/get_features_genre_and_movie?"+API_SECRECT_KEY;

    String userDetailsByID = URL+"/get_user_details_by_user_id?"+API_SECRECT_KEY;
    String latestEpisodes = URL+"get_latest_episodes?"+API_SECRECT_KEY;
    String addEpisodeToFav = URL+"add_favorite?"+API_SECRECT_KEY;
    String episodeDetails = URL+"get_single_details?"+API_SECRECT_KEY;
    String aboutUS = "http://ovoo.spagreen.net/about/";
    String sendFeedback=URL+"feedback?"+API_SECRECT_KEY;
    String watchLater = URL+"get_watch_later?"+API_SECRECT_KEY;
    String wishList = URL+"add_ep_to_wish_list?"+API_SECRECT_KEY;
    String movieWishList = URL+"add_movie_to_wish_list?"+API_SECRECT_KEY;
    String checkEpiWishList = URL+"check_ep_wish_list?"+API_SECRECT_KEY;
    String removeWishList = URL+"remove_ep_from_wish_list?"+API_SECRECT_KEY;
    String removeMovieWishList = URL+"remove_movie_from_wish_list?"+API_SECRECT_KEY;
    String checkMovieWishList = URL+"check_movie_wish_list?"+API_SECRECT_KEY;
    String searchItem = URL+"get_all_search_item?"+API_SECRECT_KEY;
    String advanceSearch = URL+"advance_search?"+API_SECRECT_KEY;

    public String getAdvanceSearch() {
        return advanceSearch;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public String getRemoveMovieWishList() {
        return removeMovieWishList;
    }

    public String getCheckMovieWishList() {
        return checkMovieWishList;
    }

    public String getRemoveWishList() {
        return removeWishList;
    }

    public String getMovieWishList() {
        return movieWishList;
    }

    public String getCheckEpiWishList() {
        return checkEpiWishList;
    }

    public String getWishList() {
        return wishList;
    }

    public String getWatchLater() {
        return watchLater;
    }

    public String getSendFeedback() {
        return sendFeedback;
    }

    public String getAboutUS() {
        return aboutUS;
    }

    public String getEpisodeDetails() {
        return episodeDetails;
    }

    public String getAddEpisodeToFav() {
        return addEpisodeToFav;
    }

    public String getLatestEpisodes() {
        return latestEpisodes;
    }

    public String getUserDetailsByID() {
        return userDetailsByID;
    }

    public String getGenreMovieURL() {
        return genreMovieURL;
    }

    public String getTermsURL() {
        return termsURL;
    }

    public String getGetAllReply() {
        return getAllReply;
    }

    public String getAddReplyURL() {
        return addReplyURL;
    }

    public String getCommentsURL() {
        return commentsURL;
    }

    public String getAddComment() {
        return addComment;
    }

    public String getRemoveFav() {
        return removeFav;
    }

    public String getFavStatusURl() {
        return favStatusURl;
    }

    public String getAddFav() {
        return addFav;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public String getProfileUpdateURL() {
        return profileUpdateURL;
    }

    public String getPassResetUrl() {
        return passResetUrl;
    }

    public String getFavoriteUrl() {
        return favoriteUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getSignup() {
        return signup;
    }

    public String getMovieByCountry() {
        return movieByCountry;
    }

    public String getMovieByGenre() {
        return movieByGenre;
    }

    public String getDetails() {
        return details;
    }

    public String getAllGenre() {
        return allGenre;
    }

    public String getAllCountry() {
        return allCountry;
    }

    public String getTvSeries() {
        return tvSeries;
    }

    public String getLatestTvSeries() {
        return latestTvSeries;
    }

    public String getSlider() {
        return slider;
    }

    public String getGet_live_tv() {
        return get_live_tv;
    }

    public String getGet_featured_tv() {
        return get_featured_tv;
    }

    public String getLatest_movie() {
        return latest_movie;
    }

    public String getGet_movie() {
        return get_movie;
    }
}
